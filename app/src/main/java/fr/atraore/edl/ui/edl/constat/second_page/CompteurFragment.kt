package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Compteur
import fr.atraore.edl.databinding.CompteurFragmentBinding
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.first_page.StartConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.COMPTEUR_LABELS
import fr.atraore.edl.utils.COMPTEUR_LABELS_LIGHT
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.compteur_fragment.*
import kotlinx.android.synthetic.main.compteur_item.view.*
import kotlinx.android.synthetic.main.end_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.log


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

    private lateinit var binding: CompteurFragmentBinding
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
            DataBindingUtil.inflate(inflater, R.layout.compteur_fragment, container, false)
        binding.compteurViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.compteurEauFroide = viewModel.compteurEauFroide
        binding.compteurElec = viewModel.compteurElec
        binding.compteurDetectFumee = viewModel.compteurDetecFumee
        binding.compteurEauChaude = viewModel.compteurEauChaude
        binding.compteurGaz = viewModel.compteurGaz
        binding.compteurCuveFioul = viewModel.compteurCuve
        binding.photoClickListener = this
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_compteur)?.isVisible = false
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
        viewModel.setCompteurs(binding)
        viewModel.constatDetail.observe(viewLifecycleOwner, { constatWithDetails ->
            constatWithDetails?.let {
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"
            }
        })
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
        when(text) {
            "Compteur d'eau chaude" -> {
                viewModel.getCompteurEauChaudeVisibility = View.VISIBLE
                ctn_compteur_eau_chaude.visibility = View.VISIBLE
                if (viewModel.compteurEauChaude == null) {
                    viewModel.saveCompteur(4)
                }
            }
            "Compteur Gaz" -> {
                viewModel.getCompteurGazVisibility = View.VISIBLE
                ctn_compteur_gaz.visibility = View.VISIBLE
                if (viewModel.compteurGaz == null) {
                    viewModel.saveCompteur(5)
                }
            }
            "Cuve à fuel / gaz" -> {
                viewModel.getCompteurCuveVisibility = View.VISIBLE
                ctn_cuve.visibility = View.VISIBLE
                if (viewModel.compteurCuve == null) {
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
            
            Glide.with(requireContext())
                .load(imagePath)
                .into(currentImageView)

            when(currentImageView.resources.getResourceEntryName(currentImageView.id)) {
                "imb_photo_eau_froide_1" -> {
                    viewModel.saveImagePathOnCompteur(1, imagePath.toString(), false)
                }
                "imb_photo_eau_froide_2" -> {
                    viewModel.saveImagePathOnCompteur(1, imagePath.toString(), true)

                }
                "imb_photo_elec_1" -> {
                    viewModel.saveImagePathOnCompteur(2, imagePath.toString(), false)
                }
                "imb_photo_elec_2" ->  {
                    viewModel.saveImagePathOnCompteur(2, imagePath.toString(), true)
                }
                "imb_photo_eau_chaude_1" ->  {
                    viewModel.saveImagePathOnCompteur(4, imagePath.toString(), false)
                }
                "imb_photo_eau_chaude_2" ->  {
                    viewModel.saveImagePathOnCompteur(4, imagePath.toString(), true)
                }
                "imb_photo_gaz_1" ->  {
                    viewModel.saveImagePathOnCompteur(5, imagePath.toString(), false)
                }
                "imb_photo_gaz_2" ->  {
                    viewModel.saveImagePathOnCompteur(5, imagePath.toString(), true)
                }
                "imb_photo_cuve_1" ->  {
                    viewModel.saveImagePathOnCompteur(6, imagePath.toString(), false)
                }
                "imb_photo_cuve_2" ->  {
                    viewModel.saveImagePathOnCompteur(6, imagePath.toString(), true)
                }
            }

        }
    }

}