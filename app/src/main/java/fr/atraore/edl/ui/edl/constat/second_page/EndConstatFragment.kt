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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.crossRef.RoomConstatCrossRef
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.databinding.FragmentEndConstatBinding
import fr.atraore.edl.ui.adapter.*
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.detail.DetailEndConstatFragment
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.*
import fr.atraore.edl.utils.itemanimators.SlideDownAlphaAnimator
import kotlinx.android.synthetic.main.fragment_end_constat.*
import kotlinx.android.synthetic.main.fragment_end_constat.imv_chauffage
import kotlinx.android.synthetic.main.fragment_end_constat.imv_elec
import kotlinx.android.synthetic.main.fragment_end_constat.imv_electromenager
import kotlinx.android.synthetic.main.fragment_end_constat.imv_lot_revetement
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

enum class ShowList {
    ROOM, KEYS, OUTDOOR
}

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

    var elementRefList: List<ElementReference>? = null

    private lateinit var binding: FragmentEndConstatBinding

    private lateinit var clickedChildItem: Detail
    private lateinit var clickedParentItem: RoomReference
    private var clickedLot: Int = 1

    private lateinit var theme: Resources.Theme
    private var showList = ShowList.ROOM

    private val roomSimpleAdapter = RoomSimpleAdapter()
    private val keySimpleAdapter = RoomSimpleAdapter()
    private val outdoorSimpleAdapter = RoomSimpleAdapter()
    private val elementSimpleAdapter = BaseRefSimpleAdapter()
    private val detailSimpleAdapter = DetailSimpleAdapter()
    private var roomsList = emptyList<RoomReference>()
    private var keysList = emptyList<KeyReference>()
    private var outdoorEqptList = emptyList<OutdoorEquipementReference>()
    private var elementList = emptyList<ElementReference>()
    private var detailList = emptyList<Detail>()
    private lateinit var currentRoomSelected: RoomReference
    private lateinit var currentKeySelected: KeyReference
    private lateinit var currentOutdoorEqptSelected: OutdoorEquipementReference
    private lateinit var currentElementSelected: ElementReference
    private lateinit var currentDetail: Detail
    private lateinit var constat: ConstatWithDetails

    private val onRoomItemClickListener = View.OnClickListener { view ->
        val roomSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = roomSimpleViewHolder.absoluteAdapterPosition
        val room = roomsList[position]
        currentRoomSelected = room

        resetSelections()

        if (roomSimpleAdapter.checkedPosition != position) {
            roomSimpleAdapter.notifyDataSetChanged()
            roomSimpleAdapter.checkedPosition = position
        }

        viewModel.getElementsRefWhereLotId(clickedLot).observeOnce(viewLifecycleOwner) { elementsRes ->
            elementList = elementsRes
            if (elementsRes.isNotEmpty()) {
                currentElementSelected = elementsRes[0]
                elementSimpleAdapter.swapData(elementsRes)
                rcv_elements.adapter = elementSimpleAdapter
                elementSimpleAdapter.setOnItemClickListener(onElementItemClickListener)
            }
        }
    }

    private val onKeyItemClickListener = View.OnClickListener { view ->
        val keySimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = keySimpleViewHolder.absoluteAdapterPosition
        val key = keysList[position]
        currentKeySelected = key

        resetSelections()

        if (keySimpleAdapter.checkedPosition != position) {
            keySimpleAdapter.notifyDataSetChanged()
            keySimpleAdapter.checkedPosition = position
        }

        viewModel.getDetailByIdKeyAndConstat(key.id, constat.constat.constatId).observeOnce(viewLifecycleOwner) { res ->
            val detail = res ?: Detail(UUID.randomUUID().toString(), null, currentRoomSelected.roomReferenceId, constat.constat.constatId, clickedLot, key.name, key.id)

            launch {
                viewModel.saveDetail(detail)
                openDetails(detail.idDetail)
            }
        }
    }

    private val onOutdoorItemClickListener = View.OnClickListener { view ->
        val outdoorSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = outdoorSimpleViewHolder.absoluteAdapterPosition
        val outdoorEqpt = outdoorEqptList[position]
        currentOutdoorEqptSelected = outdoorEqpt

        resetSelections()

        if (outdoorSimpleAdapter.checkedPosition != position) {
            outdoorSimpleAdapter.notifyDataSetChanged()
            outdoorSimpleAdapter.checkedPosition = position
        }

        viewModel.getDetailByIdOutdoorAndConstat(outdoorEqpt.id, constat.constat.constatId).observeOnce(viewLifecycleOwner) { res ->
            val detail = res ?: Detail(UUID.randomUUID().toString(), null, currentRoomSelected.roomReferenceId, constat.constat.constatId, clickedLot, outdoorEqpt.name, null, outdoorEqpt.id)

            launch {
                viewModel.saveDetail(detail)
                openDetails(detail.idDetail)
            }
        }

    }

    private val onElementItemClickListener = View.OnClickListener { view ->
        val elementSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = elementSimpleViewHolder.absoluteAdapterPosition
        val element = elementList[position]
        currentElementSelected = element

        if (elementSimpleAdapter.checkedPosition != position) {
            elementSimpleAdapter.notifyDataSetChanged()
            elementSimpleAdapter.checkedPosition = position
        }

        viewModel.getElementsRefChild(currentElementSelected.elementReferenceId).observeOnce(viewLifecycleOwner) { elements ->
            viewModel.getOrCreateDetails(elements.ifEmpty { listOf(currentElementSelected) }, clickedLot, arguments?.getString(ARGS_CONSTAT_ID)!!, currentRoomSelected.roomReferenceId).observeOnce(viewLifecycleOwner) { details ->
                if (elements.isNotEmpty()) {
                    detailList = details
                    details.getOrNull(0)?.let { currentDetail = it }
                    detailSimpleAdapter.swapData(details)
                    rcv_child.adapter = detailSimpleAdapter
                    detailSimpleAdapter.setOnItemClickListener(onChildItemClickListener)
                } else {
                    if (details.isNotEmpty()) {
                        openDetails(details[0].idDetail)
                    }
                }
            }
        }
    }

    private val onChildItemClickListener = View.OnClickListener { view ->
        val childSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = childSimpleViewHolder.absoluteAdapterPosition
        val detail = detailList[position]
        currentDetail = detail

        if (detailSimpleAdapter.checkedPosition != position) {
            detailSimpleAdapter.notifyDataSetChanged()
            detailSimpleAdapter.checkedPosition = position
        }


        openDetails(detail.idDetail)
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
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_keys)?.isVisible = true
        menu.findItem(R.id.action_outdoor)?.isVisible = true
        menu.findItem(R.id.action_compteur)?.isVisible = true
        menu.findItem(R.id.action_add_room)?.isVisible = true
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
                if (!this::currentElementSelected.isInitialized) {
                    viewModel.getAllRoomReferences().observeOnce(viewLifecycleOwner) { roomReferences ->
                        if (roomReferences.isNotEmpty()) {
                            val existingIndex = roomReferences.indices.filter { indice ->
                                roomsList.contains(roomReferences[indice])
                            }.toIntArray()

                            MaterialDialog(requireContext()).show {
                                listItemsMultiChoice(items = roomReferences.map { roomReference -> roomReference.name }, initialSelection = existingIndex) { _, _, list ->
                                    val selectedRooms = roomReferences.filter { roomRef ->
                                        list.contains(roomRef.name)
                                    }.map { roomFiltered ->
                                        RoomConstatCrossRef(roomFiltered.roomReferenceId, arguments?.getString(ARGS_CONSTAT_ID)!!)
                                    }

                                    launch {
                                        viewModel.saveRoomConstatCrossRef(selectedRooms)
                                    }
                                }
                                positiveButton(R.string.done)
                            }
                        }
                    }
                } else {
                    MaterialDialog(requireContext()).show {
                        title(R.string.add_element)
                        input(allowEmpty = false) { _, text ->

                            val detail = Detail(
                                UUID.randomUUID().toString(), currentElementSelected.elementReferenceId, currentRoomSelected.roomReferenceId, currentRoomSelected.roomReferenceId, clickedLot, text.toString()
                            )

                            launch {
                                viewModel.saveDetail(detail)
                                openDetails(detail.idDetail)
                            }
                        }
                        positiveButton(R.string.done)
                    }
                }
            }
            R.id.action_keys -> {
                resetSelections()
                //si je reclique
                if (showList.equals(ShowList.KEYS)) {
                    showList = ShowList.ROOM
                    initRooms()
                } else {
                    showList = ShowList.KEYS
                    initKeys()
                }
            }
            R.id.action_outdoor -> {
                resetSelections()
                if (showList.equals(ShowList.OUTDOOR)) {
                    showList = ShowList.ROOM
                    initRooms()
                } else {
                    showList = ShowList.OUTDOOR
                    initOutdoor()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set theme pour les lots techniques
        theme = resources.newTheme()
        //theme revetement pré sélectionné
        onLotTechniqueClick(imv_lot_revetement, 1)

        rcv_rooms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = SlideDownAlphaAnimator()
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        rcv_elements.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        }

        rcv_child.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
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
        viewModel.getRoomsForConstat(arguments?.getString(ARGS_CONSTAT_ID)!!).observe(viewLifecycleOwner) { roomRes ->
            roomsList = roomRes
            roomRes.getOrNull(0)?.let { currentRoomSelected = it }
            roomSimpleAdapter.swapData(roomRes)
            rcv_rooms.adapter = roomSimpleAdapter
            roomSimpleAdapter.setOnItemClickListener(onRoomItemClickListener)
        }
    }

    private fun initKeys() {
        viewModel.allActifKeysRef().observeOnce(viewLifecycleOwner) { keyRefs ->
            keysList = keyRefs
            keyRefs.getOrNull(0)?.let { currentKeySelected = it }
            keySimpleAdapter.swapData(keyRefs)
            rcv_rooms.adapter = keySimpleAdapter
            keySimpleAdapter.setOnItemClickListener(onKeyItemClickListener)
        }
    }

    private fun initOutdoor() {
        viewModel.allActifOutdoorRef().observeOnce(viewLifecycleOwner) { outdoorRefs ->
            outdoorEqptList = outdoorRefs
            outdoorRefs.getOrNull(0)?.let { currentOutdoorEqptSelected = it }
            outdoorSimpleAdapter.swapData(outdoorRefs)
            rcv_rooms.adapter = outdoorSimpleAdapter
            outdoorSimpleAdapter.setOnItemClickListener(onOutdoorItemClickListener)
        }
    }

    fun openDetails(itemId: String?) {
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
            resetSelections()
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
        imv_lot_revetement.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mur,
                theme
            )
        )
        imv_lot_revetement.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
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
            currentRoomSelected.roomReferenceId,
            arguments?.getString(ARGS_CONSTAT_ID)!!,
            clickedLot,
            elementReference.name
        )
    }

    private fun resetSelections() {
        elementSimpleAdapter.swapData(emptyList())
        detailSimpleAdapter.swapData(emptyList())
        openDetails(null)
    }

}