package fr.atraore.edl.ui.edl.constat.first_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.LotReference
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.databinding.StartConstatFragmentBinding
import fr.atraore.edl.ui.adapter.start.PrimaryInfoNoDataBindAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ParentItem
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.ui.hideKeyboard
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.start_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class StartConstatFragment() : BaseFragment("Constat"), View.OnClickListener, LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope {
    private val TAG = StartConstatFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = "Début de la mission"

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

    override fun goNext() {
        Log.d(TAG, "goNext: declenché")
        val bundle = bundleOf(ARGS_CONSTAT_ID to arguments?.getString(ARGS_CONSTAT_ID)!!)
        findNavController().navigate(R.id.go_to_end, bundle)
    }

    fun goCompteur() {
        val bundle = bundleOf(ARGS_CONSTAT_ID to arguments?.getString(ARGS_CONSTAT_ID)!!)
        findNavController().navigate(R.id.go_to_compteur, bundle)
    }

    @Inject
    lateinit var constatViewModelFactory: ConstatViewModel.AssistedStartFactory
    private val viewModel: ConstatViewModel by assistedViewModel {
        constatViewModelFactory.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    private lateinit var binding: StartConstatFragmentBinding
    private lateinit var constat: ConstatWithDetails
    private lateinit var listLotReference: List<LotReference>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.start_constat_fragment, container, false)
        binding.constatViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add_room)?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_compteur -> {
                goCompteur()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.constatDetail.observe(viewLifecycleOwner, {

            it?.let { constatWithDetails ->
                this.constat = constatWithDetails
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"
                configRecyclerViewsLinear(
                    rcv_tenant,
                    rcv_biens,
                    rcv_contractor,
                    rcv_owner,
                    constatWithDetails = constatWithDetails,
                    constatViewModel = viewModel
                )
            }
        })

        viewModel.initFirstRoomReference.observe(viewLifecycleOwner, {
            it.first?.let { roomRef ->
                launch {
                    viewModel.saveConstatRoomCrossRef(
                        arguments?.getString(
                            ARGS_CONSTAT_ID
                        )!!, roomRef.roomReferenceId
                    )
                }

                //set liste des elements d'une piece pour un constat
                it.second?.let { listElementReferences ->
                    listElementReferences.forEach { elementRef ->
                        viewModel.getDetailsByIdRoomAndIdConstat(
                            roomRef.roomReferenceId,
                            arguments?.getString(ARGS_CONSTAT_ID)!!
                        ).observe(viewLifecycleOwner, {
                            if (it.isNullOrEmpty() && !this@StartConstatFragment::listLotReference.isInitialized) {
                                viewModel.getAllLotReference.observe(
                                    viewLifecycleOwner, { listLotRef ->
                                        this@StartConstatFragment.listLotReference = listLotRef
                                        listLotRef.forEach {
                                            val detail = Detail(
                                                roomRef.roomReferenceId + elementRef.elementReferenceId + it.lotReferenceId,
                                                elementRef.elementReferenceId,
                                                roomRef.roomReferenceId,
                                                arguments?.getString(ARGS_CONSTAT_ID)!!,
                                                it.lotReferenceId,
                                                elementRef.name
                                            )
                                            Log.d(
                                                TAG,
                                                "onViewCreated: sauvegarde de l'item $detail"
                                            )
                                            launch {
                                                viewModel.saveDetail(detail)
                                            }
                                        }
                                    })

                            }
                        })
                    }
                }
            }
        })

        initListener()
    }

    /**
     * Initialise les events
     */
    private fun initListener() {
        imv_search_bien.setOnClickListener(this)
        imv_search_owner.setOnClickListener(this)
        imv_search_locataire.setOnClickListener(this)
        imv_search_mandataire.setOnClickListener(this)
        imv_search_agence.setOnClickListener(this)
        imv_search_user.setOnClickListener(this)

        imv_edit_owner.setOnClickListener(this)
        imv_edit_bien.setOnClickListener(this)
        imv_edit_locataire.setOnClickListener(this)
        imv_edit_mandataire.setOnClickListener(this)

        imv_add_bien.setOnClickListener(this)
        imv_add_contractor.setOnClickListener(this)
        imv_add_locataire.setOnClickListener(this)
        imv_add_owner.setOnClickListener(this)

        imv_delete_locataire.setOnClickListener(this)
        imv_delete_bien.setOnClickListener(this)
        imv_delete_mandataire.setOnClickListener(this)
        imv_delete_owner.setOnClickListener(this)

        imb_save_procuration.setOnClickListener(this)
    }

    /**
     * Configure les recyclerviews de façon lineaire
     */
    private fun configRecyclerViewsLinear(
        vararg recyclerViews: RecyclerView,
        constatWithDetails: ConstatWithDetails,
        constatViewModel: ConstatViewModel
    ) {
        recyclerViews.forEach {
            when (it.id) {
                R.id.rcv_tenant -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(
                        constatWithDetails.tenants,
                        constatViewModel
                    )
                }
                R.id.rcv_owner -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(
                        constatWithDetails.owners,
                        constatViewModel
                    )
                }
                R.id.rcv_biens -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(
                        constatWithDetails.properties,
                        constatViewModel
                    )
                }
                R.id.rcv_contractor -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(
                        constatWithDetails.contractors,
                        constatViewModel
                    )
                }
            }
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * configure l'agence et l'user
     */
    private fun configAgencyUsers(constatWithDetails: ConstatWithDetails) {

    }

    /**
     * gère les clics de l'ihm start_constat_fragment depuis l'init listener
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            //click on Item : search icon
            R.id.imv_search_owner -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_bien -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_locataire -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_mandataire -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_agence -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_AGENCES,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_user -> {
                val bundle = bundleOf(
                    ARGS_TAB_POSITION to POSITION_FRAGMENT_USER,
                    ARGS_CONSTAT to this.constat
                )
                findNavController().navigate(R.id.go_to_search, bundle)
            }

            //click on Item : Edit icon
            //click true active l'edition
            //click false sauvegarde le contenu
            R.id.imv_edit_owner -> {
                whoToEdit(this.constat.owners.map { it.name }, OWNER_LABEL)
                //editOrSave(rcv_owner.adapter as PrimaryInfoNoDataBindAdapter, v as ImageView)
            }
            R.id.imv_edit_locataire -> {
                whoToEdit(this.constat.tenants.map { it.name }, TENANT_LABEL)
                //editOrSave(rcv_tenant.adapter as PrimaryInfoNoDataBindAdapter, v as ImageView)
            }
            R.id.imv_edit_mandataire -> {
                whoToEdit(this.constat.contractors.map { it.denomination }, CONTRACTOR_LABEL)
                //editOrSave(rcv_contractor.adapter as PrimaryInfoNoDataBindAdapter, v as ImageView)
            }
            R.id.imv_edit_bien -> {
                whoToEdit(this.constat.properties.map { it.address }, PROPERTY_LABEL)
                //editOrSave(rcv_biens.adapter as PrimaryInfoNoDataBindAdapter, v as ImageView)
            }

            //Click on Add
            R.id.imv_add_bien -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS)
                findNavController().navigate(R.id.go_to_add, bundle)
            }
            R.id.imv_add_locataire -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE)
                findNavController().navigate(R.id.go_to_add, bundle)
            }
            R.id.imv_add_owner -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE)
                findNavController().navigate(R.id.go_to_add, bundle)
            }
            R.id.imv_add_contractor -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE)
                findNavController().navigate(R.id.go_to_add, bundle)
            }
            R.id.imv_delete_bien -> {
                launch {
                    viewModel.deleteConstatPropertyCrossRefByIds()
                }
            }
            R.id.imv_delete_owner -> {
                launch {
                    viewModel.deleteConstatOwnerCrossRefByIds()
                }
            }
            R.id.imv_delete_locataire -> {
                launch {
                    viewModel.deleteConstatTenantCrossRefByIds()
                }
            }
            R.id.imv_delete_mandataire -> {
                launch {
                    viewModel.deleteConstatContractorCrossRefByIds()
                }
            }

            //procuration
            R.id.imb_save_procuration -> {
                launch {
                    viewModel.saveProcuration(this@StartConstatFragment.constat.constat.constatId, edt_procuration.text.toString())
                }
            }

            else -> {
                Toast.makeText(
                    context,
                    "Une erreur est survenue. Veuillez revenir au menu principal et réessayer",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editOrSave(primaryInfoAdapter: PrimaryInfoNoDataBindAdapter, view: ImageView) {
        if (primaryInfoAdapter.edit) {
            view.setImageResource(R.drawable.ic_edit)
            if (viewModel.constatDetail.value != null) {
                primaryInfoAdapter.saveContent()
                hideKeyboard() //utilisation de l'extension pour fermer le clavier
            }
        } else {
            view.setImageResource(R.drawable.ic_edit_checked)
        }

        primaryInfoAdapter.editUpdate()
    }

    @SuppressLint("CheckResult")
    private fun whoToEdit(rows: List<String>, type: String) {
        MaterialDialog(requireContext()).show {
            title(text = "Quelle ligne voulez-vous modifier ?")
            listItemsSingleChoice(items = rows) { _, _, text ->
                when(type) {
                    PROPERTY_LABEL -> {
                        val property = constat.properties.find { it.address == text.toString() }
                        property?.let {
                            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS, ARGS_PROPERTY_ID to it.propertyId)
                            findNavController().navigate(R.id.go_to_add, bundle)
                        }
                    }
                    OWNER_LABEL -> {
                        val owner = constat.owners.find { it.name == text.toString() }
                        owner?.let {
                            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE, ARGS_OWNER_ID to it.ownerId)
                            findNavController().navigate(R.id.go_to_add, bundle)
                        }
                    }
                    CONTRACTOR_LABEL -> {
                        val tenant = constat.contractors.find { it.denomination == text.toString() }
                        tenant?.let {
                            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE, ARGS_CONTRACTOR_ID to it.contractorId)
                            findNavController().navigate(R.id.go_to_add, bundle)
                        }
                    }
                    TENANT_LABEL -> {
                        val tenant = constat.tenants.find { it.name == text.toString() }
                        tenant?.let {
                            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE, ARGS_TENANT_ID to it.tenantId)
                            findNavController().navigate(R.id.go_to_add, bundle)
                        }
                    }
                }
            }
            positiveButton(R.string.done)
        }
    }

}