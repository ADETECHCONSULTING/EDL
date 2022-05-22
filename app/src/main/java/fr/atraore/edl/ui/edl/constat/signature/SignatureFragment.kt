package fr.atraore.edl.ui.edl.constat.signature

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.ConfigPdf
import fr.atraore.edl.databinding.FragmentSignatureBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.fragment_signature.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SignatureFragment : BaseFragment("Signature"), LifecycleObserver {
    private val TAG = SignatureFragment::class.simpleName

    override val title: String
        get() = "Signature du constat"

    private lateinit var binding: FragmentSignatureBinding
    private lateinit var configPdf: ConfigPdf
    private lateinit var constat: ConstatWithDetails

    companion object {
        fun newInstance() = SignatureFragment()
    }

    override fun goNext() {

    }

    @Inject
    lateinit var signatureViewModel: SignatureViewModel.AssistedStartFactory
    private val viewModel: SignatureViewModel by assistedViewModel {
        signatureViewModel.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_compteur)?.isVisible = false
        menu.findItem(R.id.action_add_room)?.isVisible = false
        menu.findItem(R.id.action_next)?.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signature, container, false)
        binding.signatureViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //récupération du détail du constat
        viewModel.constatDetail.observeOnce(viewLifecycleOwner) { constatWithDetails ->
            this.constat = constatWithDetails

            constatWithDetails?.let {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"

                btn_save_signature.setOnClickListener { btnView ->
                    MaterialDialog(requireContext()).show {
                        positiveButton(text = "Accepter") { _ ->
                            val bitmapSignature = this@SignatureFragment.signature_pad.signatureBitmap
                            val bitmapParaph = this@SignatureFragment.signature_pad.signatureBitmap
                            if (bitmapSignature != null) {
                                InsertMedia.insertImage(requireContext().contentResolver, bitmapSignature, "${constat.constat.constatId}_Signature", "Signature image")
                            }
                            Toast.makeText(requireContext(), "La signature a bien été enregistrée", Toast.LENGTH_SHORT).show()
                        }
                        negativeButton(text = "Refuser")
                        title(text = "Conditions avant validation")
                        message(text = "${configPdf.textPdfSignature} \n ${configPdf.textPdfSignature2} \n ${configPdf.textPdfSignature3}")
                    }
                }

                imv_pdf_export.setOnClickListener { imvView ->
                    val bundle = bundleOf(
                        ARGS_CONSTAT to this.constat
                    )
                    findNavController().navigate(R.id.go_to_generate_pdf, bundle)
                }
            }
        }

        viewModel.configPdf.observe(viewLifecycleOwner) { confPdf ->
            configPdf = confPdf
        }
    }

}