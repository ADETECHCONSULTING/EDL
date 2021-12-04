package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListPopupWindow
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.databinding.CompteurFragmentBinding
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.first_page.StartConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.COMPTEUR_LABELS
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.compteur_fragment.*
import kotlinx.android.synthetic.main.compteur_item.view.*
import kotlinx.android.synthetic.main.end_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private lateinit var binding: CompteurFragmentBinding
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var currentImageButton: View

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
        binding.compteurEauFroide = viewModel.compteurEauFroid
        binding.compteurElec = viewModel.compteurElec
        binding.compteurDetectFumee = viewModel.compteurDetecFumee
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
                    listItemsSingleChoice(items = COMPTEUR_LABELS) { _, _, text ->
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
            "Compteur d'eau froide" -> {

            }
            "Compteur d'électricité" -> {

            }
            "Détecteur de fumée" -> {

            }
            "Compteur d'eau chaude" -> {

            }
            "Compteur Gaz" -> {

            }
            "Cuve à fuel / gaz" -> {

            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    private fun openPicker(view: View) {
        if (view is ImageButton) {
            currentImageButton = view
            PhotoPickerFragment.newInstance(
                multiple = true,
                allowCamera = true,
                maxSelection = 2,
                theme = R.style.ChiliPhotoPicker_Light
            ).show(childFragmentManager, "picker")
        }
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        Log.d(TAG, "onImagesPicked: $photos")
    }

}