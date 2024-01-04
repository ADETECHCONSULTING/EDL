package fr.atraore.edl.ui.edl.constat.second_page

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.data.TreeParser
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
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver, OnTreeNodeClickListener,
    MainActivity.OnNavigationFragment, CoroutineScope {
    private val TAG = EndConstatFragment::class.simpleName

    override val title: String
        get() = "Deuxieme partie du constat"

    override fun goNext() {
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private lateinit var binding: FragmentEndConstatBinding

    private var clickedLot: Int = 1

    private lateinit var theme: Resources.Theme
    private var showList = ShowList.ROOM

    private val keySimpleAdapter = RoomSimpleAdapter()
    private val outdoorSimpleAdapter = RoomSimpleAdapter()
    private val elementSimpleAdapter = BaseRefSimpleAdapter()
    private val detailSimpleAdapter = DetailSimpleAdapter()
    private var keysList = emptyList<KeyReference>()
    private var outdoorEqptList = emptyList<OutdoorEquipementReference>()
    private lateinit var currentKeySelected: KeyReference
    private lateinit var currentOutdoorEqptSelected: OutdoorEquipementReference
    private lateinit var constat: ConstatWithDetails

    override fun onNodeClicked(itemId: String?, name: String, idRoomRef: Int?) {
        viewModel.getDetailByIdEqp(itemId!!, constat.constat.constatId, clickedLot).observeOnce(viewLifecycleOwner) { res ->
            val detail = res ?: Detail(UUID.randomUUID().toString(), constat.constat.constatId, clickedLot, name, itemId)
            detail.idRoomRef = idRoomRef
            launch {
                viewModel.saveDetail(detail)
                openDetails(detail.idDetail, name)
            }
        }
    }

    override fun onNodeLongClicked(itemId: String?, name: String, idRoomRef: Int?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_element, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).setView(dialogView)

        val spinner: Spinner = dialogView.findViewById(R.id.objectSpinner)
        val inputField: EditText = dialogView.findViewById(R.id.inputField)
        var selectedIndex = idRoomRef ?: 1

        inputField.setText(name)
        val roomsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ROOMS_LABELS)
        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = roomsAdapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedIndex = position + 1 // Sauvegarder l'index sélectionné
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Gérer le cas où aucun élément n'est sélectionné si nécessaire
            }
        }

        val dialog = dialogBuilder.create()

        // Gestion des boutons
        dialogView.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            launch {
                viewModel.deleteEquipmentRef(itemId!!)
                dialog.dismiss()
            }
        }

        dialogView.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            if (inputField.text.toString().isEmpty()) {
                inputField.error = "Veuillez saisir un nom"
                return@setOnClickListener
            }

            if (selectedIndex == 0) {
                Toast.makeText(requireContext(), "Veuillez choisir une pièce", Toast.LENGTH_SHORT).show()
            }

            launch {
                viewModel.saveEquipmentRef(itemId!!, inputField.text.toString(), selectedIndex)
            }
            onNodeClicked(itemId, inputField.text.toString(), selectedIndex)
            dialog.dismiss()
        }

        dialog.show()

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
            val detail = res ?: Detail(UUID.randomUUID().toString(), constat.constat.constatId, clickedLot, key.name, null, key.id)

            launch {
                viewModel.saveDetail(detail)
                openDetails(detail.idDetail, key.name)
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
            val detail = res ?: Detail(UUID.randomUUID().toString(), constat.constat.constatId, clickedLot, outdoorEqpt.name, null, null, outdoorEqpt.id)

            launch {
                viewModel.saveDetail(detail)
                openDetails(detail.idDetail)
            }
        }

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
        menu.findItem(R.id.action_add_room)?.isVisible = false
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
        viewModel.getAllEquipments.observe(viewLifecycleOwner) { equipments ->
            val noEqpsNull = equipments.filter { it.idRoomRef != null } //TODO for now
            val treeEqps = TreeParser.buildHierarchy(noEqpsNull)
            // Assume 'rootNode' is your TreeNode with all the data populated
            val adapter = TreeNodeAdapter(treeEqps.children, 0, this)
            rcv_rooms.adapter = adapter
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

    private fun openDetails(itemId: String?, name: String? = null) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            val fragment = DetailEndConstatFragment.newInstance()
            fragment.arguments = bundleOf(
                "eqpId" to itemId,
                "idLot" to clickedLot,
                "intitule" to name,
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

    private fun resetSelections() {
        elementSimpleAdapter.swapData(emptyList())
        detailSimpleAdapter.swapData(emptyList())
        openDetails(null)
    }

}