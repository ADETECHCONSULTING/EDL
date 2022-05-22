package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.MediaStore.Images.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ListPopupWindow
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.databinding.FragmentCompteurBinding
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.first_page.StartConstatFragment
import fr.atraore.edl.ui.formatToServerDateDefaults
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.COMPTEUR_LABELS_LIGHT
import fr.atraore.edl.utils.InsertMedia
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.fragment_compteur.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class CompteurFragment : BaseFragment("Compteur"), View.OnClickListener, LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope, PhotoPickerFragment.Callback {

    private val TAG = StartConstatFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = "Compteur"

    override fun goNext() {
        Log.d(TAG, "goNext: declenché")
        findNavController().popBackStack()
    }

    companion object {
        fun newInstance() = CompteurFragment()

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageOnCompteur(view: ImageView, constat: ConstatWithDetails?) {
            var uri = Uri.EMPTY

            constat?.let { constatWithDetails ->
                when (view.resources.getResourceEntryName(view.id)) {
                    "imb_photo_eau_froide_1" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 1 }?.imagePath?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_eau_froide_2" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 1 }?.imagePathSecond?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_elec_1" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 2 }?.imagePath?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_elec_2" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 2 }?.imagePathSecond?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_eau_chaude_1" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 4 }?.imagePath?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_eau_chaude_2" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 4 }?.imagePathSecond?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_gaz_1" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 5 }?.imagePath?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_gaz_2" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 5 }?.imagePathSecond?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_cuve_1" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 6 }?.imagePath?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    "imb_photo_cuve_2" -> {
                        constatWithDetails.compteurs.find { compteur -> compteur.compteurRefId == 6 }?.imagePathSecond?.let { path ->
                            uri = Uri.parse(path)
                        }
                    }
                    else -> {}
                }
            }

            useGlide(view, uri)
        }

        private fun useGlide(imageView: ImageView, uri: Uri) {
            Glide.with(imageView.context)
                .load(uri)
                .into(imageView)
        }

    }

    private lateinit var binding: FragmentCompteurBinding
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var currentImageView: ImageView
    private lateinit var constatWithDetail: ConstatWithDetails

    @Inject
    lateinit var compteurViewModelFactory: CompteurViewModel.AssistedStartFactory
    private val viewModel: CompteurViewModel by assistedViewModel {
        compteurViewModelFactory.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_compteur, container, false)
        binding.compteurViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.photoClickListener = this
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_compteur)?.isVisible = false
        menu.findItem(R.id.action_next)?.isVisible = false
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_room -> {
                MaterialDialog(requireContext()).show {
                    title(text = "Ajouter un compteur")
                    listItemsSingleChoice(items = COMPTEUR_LABELS_LIGHT) { _, _, text ->
                        createCompteur(text.toString())
                    }
                    positiveButton(R.string.done)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCompteurs()
        viewModel.constatDetail.observe(viewLifecycleOwner) { constatWithDetails ->
            constatWithDetails?.let {
                this.constatWithDetail = constatWithDetails
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            openPicker(it)
        }
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }


    private fun createCompteur(text: String) {
        when (text) {
            "Compteur d'eau chaude" -> {
                viewModel.visibilityEauChaude.value = View.VISIBLE
                if (viewModel.compteurEauChaude.value == null) {
                    viewModel.saveCompteur(4)
                }
            }
            "Compteur Gaz" -> {
                viewModel.visibilityGaz.value = View.VISIBLE
                if (viewModel.compteurGaz.value == null) {
                    viewModel.saveCompteur(5)
                }
            }
            "Cuve à fuel / gaz" -> {
                viewModel.visibilityCuve.value = View.VISIBLE
                if (viewModel.compteurCuve.value == null) {
                    viewModel.saveCompteur(6)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCompteurs()
    }

    private fun openPicker(view: View) {
        if (view is ImageView) {
            currentImageView = view
            PhotoPickerFragment.newInstance(
                multiple = false,
                allowCamera = true,
                maxSelection = 1,
                theme = R.style.ChiliPhotoPicker_Light
            ).show(childFragmentManager, "picker")
        }
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        if (photos.isNotEmpty()) {
            val imagePath = photos[0]

            useGlide(currentImageView, imagePath)

            activity?.let {
                val absolutePathImage = InsertMedia.insertImage(it.contentResolver, getBitmapFromUri(imagePath), "${constatWithDetail.constat.constatId}_${currentImageView.resources.getResourceEntryName(currentImageView.id)}", "Compteur Image")

                absolutePathImage?.let { path ->
                    when (currentImageView.resources.getResourceEntryName(currentImageView.id)) {
                        "imb_photo_eau_froide_1" -> {
                            viewModel.saveImagePathOnCompteur(1, path, false)
                        }
                        "imb_photo_eau_froide_2" -> {
                            viewModel.saveImagePathOnCompteur(1, path, true)

                        }
                        "imb_photo_elec_1" -> {
                            viewModel.saveImagePathOnCompteur(2, path, false)
                        }
                        "imb_photo_elec_2" -> {
                            viewModel.saveImagePathOnCompteur(2, path, true)
                        }
                        "imb_photo_eau_chaude_1" -> {
                            viewModel.saveImagePathOnCompteur(4, path, false)
                        }
                        "imb_photo_eau_chaude_2" -> {
                            viewModel.saveImagePathOnCompteur(4, path, true)
                        }
                        "imb_photo_gaz_1" -> {
                            viewModel.saveImagePathOnCompteur(5, path, false)
                        }
                        "imb_photo_gaz_2" -> {
                            viewModel.saveImagePathOnCompteur(5, path, true)
                        }
                        "imb_photo_cuve_1" -> {
                            viewModel.saveImagePathOnCompteur(6, path, false)
                        }
                        "imb_photo_cuve_2" -> {
                            viewModel.saveImagePathOnCompteur(6, path, true)
                        }
                    }
                    Log.d(TAG, "onImagesPicked: Image sauvegardée")
                }
            }
        }
    }

    private fun checkIfCompteurHasImage() {
        this.constatWithDetail.compteurs.filter { compteur -> compteur.getPrimaryQuantity !== null || compteur.getSecondaryQuantity !== null }.forEach { compteur ->
            when (compteur.compteurRefId) {
                1 -> {
                    if (compteur.getPrimaryQuantity !== null) {

                    }
                    if (compteur.getSecondaryQuantity !== null) {

                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
        } else {
            Media.getBitmap(requireContext().contentResolver, imageUri)
        }
    }

}