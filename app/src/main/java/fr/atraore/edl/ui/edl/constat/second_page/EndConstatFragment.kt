package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.checkItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.mikepenz.fastadapter.expandable.getExpandableExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.databinding.FragmentEndConstatBinding
import fr.atraore.edl.ui.adapter.DetailSimpleAdapter
import fr.atraore.edl.ui.adapter.ElementSimpleAdapter
import fr.atraore.edl.ui.adapter.RoomSimpleAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.detail.DetailEndConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.*
import fr.atraore.edl.utils.itemanimators.SlideDownAlphaAnimator
import kotlinx.android.synthetic.main.activity_room_configuration.*
import kotlinx.android.synthetic.main.fragment_end_constat.*
import kotlinx.android.synthetic.main.fragment_end_constat.imv_chauffage
import kotlinx.android.synthetic.main.fragment_end_constat.imv_elec
import kotlinx.android.synthetic.main.fragment_end_constat.imv_electromenager
import kotlinx.android.synthetic.main.fragment_end_constat.imv_lot_batis
import kotlinx.android.synthetic.main.fragment_end_constat.imv_meulbe
import kotlinx.android.synthetic.main.fragment_end_constat.imv_mobilier
import kotlinx.android.synthetic.main.fragment_end_constat.imv_ouvrants
import kotlinx.android.synthetic.main.fragment_end_constat.imv_plomberie
import kotlinx.android.synthetic.main.fragment_end_constat.rcv_rooms
import kotlinx.android.synthetic.main.fragment_end_constat.rcv_elements
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope {
    private val TAG = EndConstatFragment::class.simpleName

    private lateinit var fastItemAdapter: GenericFastItemAdapter

    override val title: String
        get() = "Deuxieme partie du constat"

    override fun goNext() {
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

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

    private val roomSimpleAdapter = RoomSimpleAdapter()
    private val elementSimpleAdapter = ElementSimpleAdapter()
    private val detailSimpleAdapter = DetailSimpleAdapter()
    private var roomsList = emptyList<RoomReference>()
    private var elementList = emptyList<ElementReference>()
    private var detailList = emptyList<Detail>()
    private lateinit var currentRoomSelected: RoomReference
    private lateinit var currentElementSelected: ElementReference
    private lateinit var currentDetail: Detail
    private lateinit var constat: ConstatWithDetails

    private val onRoomItemClickListener = View.OnClickListener { view ->
        val roomSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = roomSimpleViewHolder.absoluteAdapterPosition
        val room = roomsList[position]
        currentRoomSelected = room

        viewModel.getElementsRefWhereRoomId(room.roomReferenceId).observeOnce(viewLifecycleOwner, { elementsRes ->
            elementList = elementsRes
            currentElementSelected = elementsRes[0]
            elementSimpleAdapter.swapData(elementsRes)
            rcv_elements.adapter = elementSimpleAdapter
            elementSimpleAdapter.setOnItemClickListener(onElementItemClickListener)
        })
    }

    private val onElementItemClickListener = View.OnClickListener { view ->
        val elementSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = elementSimpleViewHolder.absoluteAdapterPosition
        val element = elementList[position]
        currentElementSelected = element

        viewModel.getDetailsByRoomRefElementIdIdLot(currentRoomSelected.roomReferenceId, currentElementSelected.elementReferenceId, clickedLot).observe(viewLifecycleOwner, { detailsRes ->
            detailList = detailsRes
            detailsRes.getOrNull(0)?.let { currentDetail = it }
            detailSimpleAdapter.swapData(detailsRes)
            rcv_child.adapter = detailSimpleAdapter
            detailSimpleAdapter.setOnItemClickListener(onChildItemClickListener)
        })
    }

    private val onChildItemClickListener = View.OnClickListener { view ->
        val childSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = childSimpleViewHolder.absoluteAdapterPosition
        val detail = detailList[position]
        currentDetail = detail

        Toast.makeText(requireContext(), "YEAAAH", Toast.LENGTH_SHORT).show()
    }

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
                MaterialDialog(requireContext()).show {
                    title(R.string.add_element)
                    input(allowEmpty = false) { _, text ->

                        if (currentElementSelected == null) {
                            Toast.makeText(requireContext(), "Veuillez sélectionner un élément", Toast.LENGTH_SHORT).show()
                            return@input
                        }

                        if (currentRoomSelected == null) {
                            Toast.makeText(requireContext(), "Veuillez sélectionner une pièce", Toast.LENGTH_SHORT).show()
                            return@input
                        }

                        if (clickedLot == null) {
                            Toast.makeText(requireContext(), "Veuillez sélectionner un lot", Toast.LENGTH_SHORT).show()
                            return@input
                        }

                        val detail = Detail(UUID.randomUUID().toString(), currentElementSelected.elementReferenceId, currentRoomSelected.roomReferenceId,
                            constat.constat.constatId, clickedLot, text.toString())

                        launch {
                            viewModel.saveDetail(detail)
                            openDetails(detail.idDetail)
                        }
                    }
                    positiveButton(R.string.done)
                }
            }
            R.id.action_keys -> {
//                if (keysSelected) {
//                    initExpendableList()
//                } else {
//                    initKeysList()
//                }
                keysSelected = !keysSelected
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set theme pour les lots techniques
        theme = resources.newTheme()
        //theme batis pré sélectionné
        onLotTechniqueClick(imv_lot_batis, 1)

//        //register dropdown
//        listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
//        listPopupWindow.anchorView = rcv_rooms
//
//        val items = listOf("Renommer", "Supprimer")
//        val adapterDropD = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
//        listPopupWindow.setAdapter(adapterDropD)
//
//        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, anchorView: View?, position: Int, id: Long ->
//            // Respond to list popup window item click.
//
//            when (position) {
//                0 -> rename()
//                1 -> delete()
//            }
//
//            // Dismiss popup.
//            listPopupWindow.dismiss()
//        }

        rcv_rooms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastItemAdapter
            itemAnimator = SlideDownAlphaAnimator()
        }

        //récupération du détail du constat
        viewModel.constatDetail.observeOnce(viewLifecycleOwner) { constatWithDetails ->
            constatWithDetails?.let {
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"

                constat = it
            }
        }

        initRooms()
    }

    private fun initRooms() {
        viewModel.getRoomsWithIdLotAndWithElementsExist(clickedLot).observeOnce(this) { roomRes ->
            roomsList = roomRes
            currentRoomSelected = roomRes[0]
            roomSimpleAdapter.swapData(roomRes)
            rcv_rooms.adapter = roomSimpleAdapter
            roomSimpleAdapter.setOnItemClickListener(onRoomItemClickListener)
        }
    }

    /*
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
                parentListItems.add(parentIt)
            }

            fastItemAdapter.setNewList(parentListItems)

        }
    }

     */

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
        }
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
            initRooms()
            elementSimpleAdapter.swapData(emptyList())
            detailSimpleAdapter.swapData(emptyList())
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

    private fun onChildClick(detail: Detail) {
        viewModel.getDetailById(detail.idDetail).observeOnce(viewLifecycleOwner) { detailSaved ->
            if (detailSaved == null) {
                launch {
                    viewModel.saveDetail(detail)
                }
            }
            openDetails(detail.idDetail)
        }
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