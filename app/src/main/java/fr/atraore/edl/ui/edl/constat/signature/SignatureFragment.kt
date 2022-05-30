package fr.atraore.edl.ui.edl.constat.signature

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.ConfigPdf
import fr.atraore.edl.databinding.FragmentSignatureBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.ui.pdf.PdfConstatCreatorActivity
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.InsertMedia
import fr.atraore.edl.utils.assistedViewModel
import fr.atraore.edl.utils.observeOnce
import kotlinx.android.synthetic.main.fragment_signature.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignatureFragment : BaseFragment("Signature"), LifecycleObserver, CoroutineScope {
    private val TAG = SignatureFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


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
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"
            }

        }


        imv_pdf_export.setOnClickListener { btnView ->
            MaterialDialog(requireContext()).show {
                positiveButton(text = "Accepter") { _ ->
                    saveCanvas()
                    val intent = Intent(activity, PdfConstatCreatorActivity::class.java)
                    intent.putExtra("constatId", arguments?.getString(ARGS_CONSTAT_ID)!!)
                    startActivity(intent)
                }
                negativeButton(text = "Refuser")
                title(text = "Conditions avant validation")
                message(text = "${configPdf.textPdfSignature} \n ${configPdf.textPdfSignature2} \n ${configPdf.textPdfSignature3}")
            }
        }

        viewModel.configPdf.observe(viewLifecycleOwner) { confPdf ->
            configPdf = confPdf
        }
    }

    private fun saveCanvas() {
        launch {
            val bitmapSignatureOwner = this@SignatureFragment.signature_pad.signatureBitmap
            val bitmapSignatureTenant = this@SignatureFragment.signature_pad_tenant.signatureBitmap
            val bitmapParaph = this@SignatureFragment.paraph_pad.signatureBitmap

            if (bitmapSignatureOwner != null) {
                val absolutePathImage = InsertMedia.insertImage(requireContext().contentResolver, bitmapSignatureOwner, "${constat.constat.constatId}_SignatureProprietaire", "Signature image")
                absolutePathImage?.let { viewModel.saveOwnerSignaturePath(absolutePathImage, constat.constat.constatId) }
            }
            if (bitmapSignatureTenant != null) {
                val absolutePathImage = InsertMedia.insertImage(requireContext().contentResolver, bitmapSignatureTenant, "${constat.constat.constatId}_SignatureLocataire", "Signature image")
                absolutePathImage?.let { viewModel.saveTenantSignaturePath(absolutePathImage, constat.constat.constatId) }
            }
            if (bitmapParaph != null) {
                val absolutePathImage = InsertMedia.insertImage(requireContext().contentResolver, bitmapParaph, "${constat.constat.constatId}_Paraphe", "Paraphe image")
                absolutePathImage?.let { viewModel.saveParaphPath(absolutePathImage, constat.constat.constatId) }
            }
            Toast.makeText(requireContext(), "La signature a bien été enregistrée", Toast.LENGTH_SHORT).show()
        }
    }

}