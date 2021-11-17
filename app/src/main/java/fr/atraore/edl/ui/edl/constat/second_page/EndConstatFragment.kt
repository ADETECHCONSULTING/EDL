package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.sax.Element
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListPopupWindow
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.databinding.EndConstatFragmentBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ChildItem
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ParentItem
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.TwoPaneOnBackPressedCallback
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.end_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope, ChildItem.IActionHandler, ParentItem.IActionHandler {
    private val TAG = EndConstatFragment::class.simpleName

    override val title: String
        get() = "Deuxieme partie du constat"

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var groupLayoutManager: LinearLayoutManager

    private var parentList: MutableList<ParentItem> = mutableListOf()
    var roomRefList: List<RoomReference>? = null
    var elementRefList: List<ElementReference>? = null

    private lateinit var binding: EndConstatFragmentBinding

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var clickedChildItem: ElementReference
    private lateinit var clickedParentItem: RoomReference

    private lateinit var theme: Resources.Theme

    var listener: ((List<ParentItem>) -> Unit)? = null

    companion object {
        fun newInstance() = EndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    @Inject
    lateinit var constatViewModelFactory: ConstatViewModel.AssistedStartFactory
    private val viewModel: ConstatViewModel by assistedViewModel {
        constatViewModelFactory.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.end_constat_fragment, container, false)
        binding.constatViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        return binding.root
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            TwoPaneOnBackPressedCallback(sliding_pane_layout)
        )

        //set theme pour les lots techniques
        theme = resources.newTheme()
        unClickAllLotTechnique(theme)

        //register dropdown
        listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = rcv_rooms

        val items = listOf("Renommer", "Supprimer")
        val adapterDropD = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapterDropD)

        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, anchorView: View?, position: Int, id: Long ->
            // Respond to list popup window item click.

            when (position) {
                0 -> rename()
                1 -> delete()
            }

            // Dismiss popup.
            listPopupWindow.dismiss()
        }

        listener = {
            val expandableGroups = mutableListOf<ExpandableGroup>()
            it.forEach { parentItem ->
                //ajout à la liste expandable
                expandableGroups.add(ExpandableGroup(parentItem, false).apply {
                    addAll(parentItem.childItems)
                })
            }
            groupAdapter.updateAsync(expandableGroups)
        }

        btn_add_room.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(R.string.title_add_rooms)
                listItemsMultiChoice(items = roomRefList?.map { roomReference -> roomReference.name }) { _, _, items ->
                    val roomsToAdd: List<RoomReference>? =
                        roomRefList?.filter { roomRef -> roomRef.name in items }
                    roomsToAdd?.forEach {
                        launch {
                            viewModel.saveConstatRoomCrossRef(
                                arguments?.getString(ARGS_CONSTAT_ID)!!,
                                it.roomReferenceId
                            )

                            elementRefList?.forEach { elementRef ->
                                launch {
                                    viewModel.saveRoomElementCrossRef(it.roomReferenceId, elementRef.elementReferenceId, null)
                                }
                            }
                        }
                    }
                }
                lifecycleOwner(this@EndConstatFragment)
                positiveButton(R.string.done)
            }
        }

        groupLayoutManager = LinearLayoutManager(activity)

        rcv_rooms.apply {
            layoutManager = groupLayoutManager
            adapter = groupAdapter
        }

        //récupération du détail du constat
        viewModel.constatDetail.observe(viewLifecycleOwner, { constatWithDetails ->
            constatWithDetails?.let {
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"

                //récupération de toutes les pièces de ce constat
                //Pour chaque pièces du constat, récupérer les éléments et les affecter dans l'expandable list
                viewModel.roomCombinedLiveData().observe(viewLifecycleOwner, { pairInfoRoom ->
                    pairInfoRoom.first?.let { roomsWithElements ->
                        val parentListItems = mutableListOf<ParentItem>()
                        roomsWithElements.filter { rse -> rse.elements.size > 0 }.forEach {
                            val parentIt = ParentItem(it.rooms, this)
                            parentIt.childItems = it.elements.map { elementRef ->
                                ChildItem(elementRef, this@EndConstatFragment)
                            }

                            if (parentIt.roomParent.roomReferenceId !in parentListItems.map { re -> re.roomParent }.map { roomReference -> roomReference.roomReferenceId } ) {
                                parentListItems.add(parentIt)
                            }
                        }
                        listener?.invoke(parentListItems)

                        pairInfoRoom.second?.let { listRoomReference ->
                            this.roomRefList = listRoomReference
                            btn_add_room.isEnabled = this.roomRefList?.isEmpty() != true
                        }

                        pairInfoRoom.third?.let { listElementReference ->
                            this.elementRefList = listElementReference
                        }
                    }
                })
            }
        })

        viewModel.allElementsWithRefsEtat.observe(viewLifecycleOwner, { elementWithRefsEtats ->
            Log.d(TAG, "onViewCreated: element $elementWithRefsEtats")
        })
    }

    // A method on the Fragment that owns the SlidingPaneLayout,
    // called by the adapter when an item is selected.
    fun openDetails(itemId: String) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            val fragment = DetailEndConstatFragment.newInstance()
            fragment.arguments = bundleOf("itemId" to itemId)
            replace(R.id.fragment_detail, fragment)
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (sliding_pane_layout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        sliding_pane_layout.open()
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

    override fun onLongClick(anchorView: View, elementReference: ElementReference) {
        clickedChildItem = elementReference
        listPopupWindow.anchorView = anchorView
        listPopupWindow.show()

    }

    @SuppressLint("CheckResult")
    private fun rename() {
        MaterialDialog(requireContext()).show {
            title(R.string.rename_dialog_title)
            input(prefill = clickedChildItem.name, allowEmpty = false) { _, text ->
                clickedChildItem.name = text.toString()
                launch {
                    viewModel.saveRoomElementCrossRef(clickedParentItem.roomReferenceId, clickedChildItem.elementReferenceId, clickedChildItem.name)
                }
            }
            positiveButton(R.string.rename)
        }
    }

    private fun delete() {
        MaterialDialog(requireContext()).show {
            title(R.string.delete_dialog_title)
            message(R.string.delete_dialog_content)
            positiveButton(R.string.done) {
                launch {
                    viewModel.deleteRoomElementCrossRef(clickedParentItem.roomReferenceId, clickedChildItem.elementReferenceId)
                }
            }
            negativeButton(R.string.cancel_label) {
                dismiss()
            }
        }
    }

    //click on child
    override fun onSimpleClick(elementReference: ElementReference) {
        openDetails(elementReference.elementReferenceId)
    }

    //click on parent
    override fun onSimpleClick(roomParent: RoomReference) {
        clickedParentItem = roomParent
    }

    //click on lot technique
    fun onLotTechniqueClick(view: View, idLot: Int) {
        unClickAllLotTechnique(theme)
        val themeClick = resources.newTheme()
        themeClick.applyStyle(R.style.ClickedLot, false)
        changeTheme(themeClick, view as ImageView, idLot)
    }

    private fun changeTheme(theme: Resources.Theme, view: ImageView, idLot: Int) {
        val drawable = drawableLotTechnique(idLot, theme)
        view.setImageDrawable(drawable)
    }

    private fun drawableLotTechnique(idLot: Int, theme: Resources.Theme): Drawable? {
        return when(idLot) {
            1 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_mur, theme)
            2 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_ouvrant, theme)
            3 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme)
            4 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_plomberie, theme)
            5 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_chauffage, theme)
            6 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_electromenager, theme)
            7 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_mobilier, theme)
            8 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_meuble, theme)
            else -> null
        }
    }

    private fun unClickAllLotTechnique(theme: Resources.Theme) {
        theme.applyStyle(R.style.DefaultLot, false)
        imv_lot_batis.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_mur, theme))
        imv_ouvrants.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_ouvrant, theme))
        imv_elec.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme))
        imv_plomberie.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_plomberie, theme))
        imv_chauffage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_chauffage, theme))
        imv_electromenager.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_electromenager, theme))
        imv_mobilier.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_mobilier, theme))
        imv_meulbe.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_meuble, theme))
    }


}