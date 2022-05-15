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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.databinding.FragmentCompteurBinding
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.first_page.StartConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.COMPTEUR_LABELS_LIGHT
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
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
    }

    private lateinit var binding: FragmentCompteurBinding
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var currentImageView: ImageView

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
            var imagePath = photos[0]

            Glide.with(requireContext())
                .load(imagePath)
                .into(currentImageView)

            activity?.let {
                val absolutePathImage = insertImage(it.contentResolver, getBitmapFromUri(imagePath), "TEST", "TEST")

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

    private fun getBitmapFromUri(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
        } else {
            Media.getBitmap(requireContext().contentResolver, imageUri)
        }
    }

    fun insertImage(
        cr: ContentResolver,
        source: Bitmap?,
        title: String?,
        description: String?
    ): String? {
        val values = ContentValues()
        values.put(Media.TITLE, title)
        values.put(Media.DISPLAY_NAME, title)
        values.put(Media.DESCRIPTION, description)
        values.put(Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(Media.DATE_ADDED, System.currentTimeMillis())
        values.put(Media.DATE_TAKEN, System.currentTimeMillis())
        var url: Uri? = null
        var stringUrl: String? = null /* value to be returned */
        try {
            url = cr.insert(Media.EXTERNAL_CONTENT_URI, values)
            if (source != null) {
                val imageOut: OutputStream? = cr.openOutputStream(url!!)
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
                } finally {
                    imageOut?.close()
                }
                val id = ContentUris.parseId(url)
                // Wait until MINI_KIND thumbnail is generated.
                val miniThumb: Bitmap = Thumbnails.getThumbnail(cr, id, Thumbnails.MINI_KIND, null)
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50f, 50f, Thumbnails.MICRO_KIND)
            } else {
                cr.delete(url!!, null, null)
                url = null
            }
        } catch (e: Exception) {
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }
        if (url != null) {
            stringUrl = url.toString()
        }
        return stringUrl
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media
     */
    private fun storeThumbnail(
        cr: ContentResolver,
        source: Bitmap,
        id: Long,
        width: Float,
        height: Float,
        kind: Int
    ): Bitmap? {

        // create the matrix to scale it
        val matrix = Matrix()
        val scaleX = width / source.width
        val scaleY = height / source.height
        matrix.setScale(scaleX, scaleY)
        val thumb = Bitmap.createBitmap(
            source, 0, 0,
            source.width,
            source.height, matrix,
            true
        )
        val values = ContentValues(4)
        values.put(Thumbnails.KIND, kind)
        values.put(Thumbnails.IMAGE_ID, id.toInt())
        values.put(Thumbnails.HEIGHT, thumb.height)
        values.put(Thumbnails.WIDTH, thumb.width)
        val url = cr.insert(Thumbnails.EXTERNAL_CONTENT_URI, values)
        return try {
            val thumbOut: OutputStream? = cr.openOutputStream(url!!)
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
            thumbOut?.close()
            thumb
        } catch (ex: FileNotFoundException) {
            null
        } catch (ex: IOException) {
            null
        }
    }

}