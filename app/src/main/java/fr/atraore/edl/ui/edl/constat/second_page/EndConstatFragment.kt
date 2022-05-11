package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.checkItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.google.android.material.checkbox.MaterialCheckBox
import com.mikepenz.fastadapter.expandable.getExpandableExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.adaptermodel.KeyParentItem
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.data.models.adaptermodel.SimpleParentExpandableItem
import fr.atraore.edl.data.models.adaptermodel.SimpleSubItem
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.databinding.FragmentEndConstatBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.detail.DetailEndConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.*
import fr.atraore.edl.utils.itemanimators.SlideDownAlphaAnimator
import kotlinx.android.synthetic.main.fragment_end_constat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope, SimpleSubItem.IActionHandler, SimpleParentExpandableItem.IActionHandler {
    private val TAG = EndConstatFragment::class.simpleName

    private lateinit var fastItemAdapter: GenericFastItemAdapter

    override val title: String
        get() = "Deuxieme partie du constat"

    override fun goNext() {
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var roomRefList: List<RoomReference>? = null
    var elementRefList: List<ElementReference>? = null
    private lateinit var roomsWithElements: List<RoomWithElements>

    private lateinit var binding: FragmentEndConstatBinding

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var clickedChildItem: Detail
    private lateinit var clickedParentItem: RoomReference
    private var clickedLot: Int = 1

    private lateinit var theme: Resources.Theme
    private var keysSelected = false

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_end_constat, container, false)
        binding.constatViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this

        //init fastAdapter
        fastItemAdapter = FastItemAdapter()
        fastItemAdapter.getExpandableExtension()
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true
        selectExtension.selectOnLongClick = true

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_keys)?.isVisible = true
        menu.findItem(R.id.action_compteur)?.isVisible = true
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_next -> {
                val bundle = bundleOf(ARGS_CONSTAT_ID to arguments?.getString(ARGS_CONSTAT_ID)!!)
                findNavController().navigate(R.id.go_to_signature, bundle)
            }
            R.id.action_compteur -> {
                val bundle = bundleOf(ARGS_CONSTAT_ID to arguments?.getString(ARGS_CONSTAT_ID)!!)
                findNavController().navigate(R.id.go_to_compteur, bundle)
            }
            R.id.action_add_room -> {
                val indexes = mutableListOf<Int>()
                roomsWithElements.forEach { roomWithDetails ->
                    if (roomWithDetails.elements.isNotEmpty()) {
                        roomRefList?.map { ref -> ref.name }?.indexOf(roomWithDetails.room.name)?.let { index ->
                            indexes.add(index)
                        }
                    }
                }
                val dialog = MaterialDialog(requireContext()).show {
                    title(R.string.title_add_rooms)
                    listItemsMultiChoice(items = roomRefList?.map { roomReference -> roomReference.name }) { _, _, items ->
                        val roomsToAdd: List<RoomReference>? =
                            roomRefList?.filter { roomRef -> roomRef.name in items }
                        val roomsToCompare = roomsWithElements.filter { rm -> rm.elements.isNotEmpty() }
                            .map { rm2 -> rm2.room }//récupérer que les pièces qui ont des éléments

                        //check des rooms à supprimer
                        roomsToCompare.forEach { roomToCompare ->
                            if (roomsToAdd != null) {
                                if (roomToCompare !in roomsToAdd) {
                                    //delete
                                    launch {
                                        viewModel.deleteConstatRoomCrossRef(
                                            arguments?.getString(ARGS_CONSTAT_ID)!!,
                                            roomToCompare.roomReferenceId,
                                            clickedLot
                                        )

                                        viewModel.deleteAllDetailsFromRoom(roomToCompare.roomReferenceId)
                                    }
                                }
                            }
                        }

                        roomsToAdd?.forEach { roomToAdd ->
                            if (roomToAdd !in roomsToCompare) {
                                var roomToAddTemp = roomToAdd
                                viewModel.getRoomWithNameAndIdLot(roomToAdd.name, clickedLot).observeOnce(viewLifecycleOwner) { roomRefIfExist ->
                                    launch {
                                        if (roomRefIfExist == null) {
                                            val roomReference = RoomReference(UUID.randomUUID().toString(), roomToAdd.name, clickedLot)
                                            if (roomReference.name == "ACCES / ENTREE") {
                                                roomReference.mandatory = true
                                            }
                                            viewModel.saveRoom(roomReference)
                                            roomToAddTemp = roomReference
                                        } else {
                                            roomToAddTemp = roomRefIfExist
                                        }

                                        viewModel.saveConstatRoomCrossRef(
                                            arguments?.getString(ARGS_CONSTAT_ID)!!,
                                            roomToAddTemp.roomReferenceId,
                                            clickedLot
                                        )
                                    }
                                }
                            }
                        }
                    }
                    lifecycleOwner(this@EndConstatFragment)
                    positiveButton(R.string.done)
                }
                dialog.checkItems(indexes.toIntArray())
            }
            R.id.action_keys -> {
                if (keysSelected) {
                    initExpendableList()
                } else {
                    initKeysList()
                }
                keysSelected = !keysSelected
            }
        }
        return super.onOptionsItemSelected(item)
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
        //theme batis pré sélectionné
        onLotTechniqueClick(imv_lot_batis, 1)

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

        rcv_rooms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastItemAdapter
            itemAnimator = SlideDownAlphaAnimator()
        }

        //récupération du détail du constat
        viewModel.constatDetail.observe(viewLifecycleOwner) { constatWithDetails ->
            constatWithDetails?.let {
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"

                //récupération de toutes les pièces de ce constat
                //Pour chaque pièces du constat, récupérer les éléments et les affecter dans l'expandable list
                initExpendableList();
            }
        }
    }

    private fun initExpendableList() {
        viewModel.roomCombinedLiveData(clickedLot).observe(viewLifecycleOwner) { pairInfoRoom ->
            Log.d(TAG, "onViewCreated: CLICKED LOT $clickedLot")
            val identifier = AtomicLong(1)
            pairInfoRoom.first?.let { roomsWithElements ->
                val parentListItems = mutableListOf<SimpleParentExpandableItem>()
                this@EndConstatFragment.roomsWithElements = roomsWithElements

                roomsWithElements.forEach { roomWithEl ->
                    //Parent
                    val parentIt = SimpleParentExpandableItem().withHeader(roomWithEl.room.name)
                    parentIt.identifier = identifier.getAndIncrement()
                    roomWithEl.elements.sortedBy { value -> value.name }

                    //Enfant
                    val subItems = roomWithEl.elements.map { element ->
                        val detail = createDetail(roomWithEl.room, element)
                        SimpleSubItem(this).withHeader(detail)
                    }

                    subItems.forEach { subItem ->
                        run {
                            subItem.identifier = identifier.getAndIncrement()
                        }
                    }
                    parentIt.subItems.addAll(subItems)

                    parentListItems.add(parentIt)
                }

                fastItemAdapter.setNewList(parentListItems)

                pairInfoRoom.second?.let { listRoomReference ->
                    this.roomRefList = listRoomReference
                }

                pairInfoRoom.third?.let { listElementReference ->
                    this.elementRefList = listElementReference
                }
            }
        }
    }

    private fun initKeysList() {
        viewModel.allActifKeysRef().observeOnce(viewLifecycleOwner) { keyRefs ->
            val identifier = AtomicLong(1)
            val parentListItems = mutableListOf<SimpleParentExpandableItem>()

            keyRefs.forEach { it ->
                //Parent
                val detail = Detail(
                    it.id.toString() + arguments?.getString(ARGS_CONSTAT_ID)!!, //pas les mêmes infos que le detail room
                    null,
                    null,
                    arguments?.getString(ARGS_CONSTAT_ID)!!,
                    clickedLot,
                    it.name,
                    it.id //id key
                )
                val parentIt = SimpleParentExpandableItem()
                    .withHeader(it.name)
                    .withDetail(detail)

                parentIt.identifier = identifier.getAndIncrement()
                parentIt.actionHandler = this
                parentListItems.add(parentIt)
            }

            fastItemAdapter.setNewList(parentListItems)

        }
    }

    // A method on the Fragment that owns the SlidingPaneLayout,
    // called by the adapter when an item is selected.
    fun openDetails(itemId: String) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            val fragment = DetailEndConstatFragment.newInstance()
            fragment.arguments = bundleOf(
                "detailId" to itemId,
                "idLot" to clickedLot,
                ARGS_CONSTAT_ID to arguments?.getString(ARGS_CONSTAT_ID)!!
            )
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

    @SuppressLint("CheckResult")
    private fun rename() {
        MaterialDialog(requireContext()).show {
            title(R.string.rename_dialog_title)
            input(prefill = clickedChildItem.intitule, allowEmpty = false) { _, text ->
                launch {
                    clickedChildItem.intitule = text.toString()
                    viewModel.saveDetail(clickedChildItem)
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
                    viewModel.deleteRoomDetailCrossRef(
                        clickedParentItem.roomReferenceId,
                        clickedChildItem.idDetail
                    )
                }
            }
            negativeButton(R.string.cancel_label) {
                dismiss()
            }
        }
    }

    //click on lot technique
    fun onLotTechniqueClick(view: View, idLot: Int) {
        unClickAllLotTechnique(theme)
        val themeClick = resources.newTheme()
        themeClick.applyStyle(R.style.ClickedLot, false)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        changeTheme(themeClick, view as ImageView, idLot)
        if (clickedLot != idLot) {
            clickedLot = idLot
            initExpendableList()
        }
    }

    private fun changeTheme(theme: Resources.Theme, view: ImageView, idLot: Int) {
        val drawable = drawableLotTechnique(idLot, theme)
        view.setImageDrawable(drawable)
    }

    private fun drawableLotTechnique(idLot: Int, theme: Resources.Theme): Drawable? {
        return when (idLot) {
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
        imv_lot_batis.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mur,
                theme
            )
        )
        imv_lot_batis.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_ouvrants.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_ouvrant,
                theme
            )
        )
        imv_ouvrants.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_elec.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme))
        imv_elec.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_plomberie.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_plomberie,
                theme
            )
        )
        imv_plomberie.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_chauffage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_chauffage,
                theme
            )
        )
        imv_chauffage.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_electromenager.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_electromenager,
                theme
            )
        )
        imv_electromenager.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_mobilier.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mobilier,
                theme
            )
        )
        imv_mobilier.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        imv_meulbe.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_meuble,
                theme
            )
        )
        imv_meulbe.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    override fun onSimpleClick(detail: Detail) {
        viewModel.getDetailById(detail.idDetail).observeOnce(viewLifecycleOwner) { detailSaved ->
            if (detailSaved == null) {
                launch {
                    viewModel.saveDetail(detail)
                }
            }
            openDetails(detail.idDetail)
        }
    }

    override fun onLongClick(anchorView: View, detail: Detail) {
        clickedChildItem = detail
        listPopupWindow.anchorView = anchorView
        listPopupWindow.show()
    }

    override fun onSimpleKeyClick(detail: Detail) {
        onSimpleClick(detail)
    }

    private fun createDetail(roomReference: RoomReference, elementReference: ElementReference): Detail {
        return Detail(
            roomReference.roomReferenceId + elementReference.elementReferenceId + clickedLot,
            elementReference.elementReferenceId,
            roomReference.roomReferenceId,
            arguments?.getString(ARGS_CONSTAT_ID)!!,
            clickedLot,
            elementReference.name
        )
    }

}