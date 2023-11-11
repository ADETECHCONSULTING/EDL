package fr.atraore.edl.ui.edl.constat.first_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.LotReference
import fr.atraore.edl.data.models.entity.PrimaryInfo
import fr.atraore.edl.databinding.FragmentStartConstatBinding
import fr.atraore.edl.ui.adapter.start.PrimaryInfoNoDataBindAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.fragment_start_constat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class StartConstatFragment() : BaseFragment("Constat"), View.OnClickListener, LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope {
    private val TAG = StartConstatFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

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

    private lateinit var binding: FragmentStartConstatBinding
    private lateinit var constat: ConstatWithDetails
    private lateinit var listLotReference: List<LotReference>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_start_constat, container, false)
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
        menu.findItem(R.id.action_previous)?.isVisible = true
        menu.findItem(R.id.action_compteur)?.isVisible = true
        menu.findItem(R.id.action_next)?.isVisible = true
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

        viewModel.constatDetail.observe(viewLifecycleOwner) {

            it?.let { constatWithDetails ->
                this.constat = constatWithDetails
                viewModel.constatHeaderInfo.value =
                    "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"

                // Usage:
                val dataMap = mapOf(
                    R.id.rcv_tenant to constatWithDetails.tenants,
                    R.id.rcv_owner to constatWithDetails.owners,
                    R.id.rcv_biens to constatWithDetails.properties,
                    R.id.rcv_contractor to constatWithDetails.contractors
                )
                configRecyclerViewsLinear(rcv_tenant, rcv_biens, rcv_contractor, rcv_owner, dataMap = dataMap)
            }
        }

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
        dataMap: Map<Int, List<PrimaryInfo>>
    ) {
        recyclerViews.forEach { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = dataMap[recyclerView.id]?.let {
                PrimaryInfoNoDataBindAdapter(it, viewModel)
            }
        }
    }

    /**
     * gère les clics de l'ihm start_constat_fragment depuis l'init listener
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imv_search_owner, R.id.imv_search_bien, R.id.imv_search_locataire,
            R.id.imv_search_mandataire, R.id.imv_search_agence, R.id.imv_search_user -> {
                handleSearchClick(v.id)
            }
            R.id.imv_edit_owner, R.id.imv_edit_locataire, R.id.imv_edit_mandataire,
            R.id.imv_edit_bien -> {
                handleEditClick(v.id)
            }
            R.id.imv_add_bien, R.id.imv_add_locataire, R.id.imv_add_owner,
            R.id.imv_add_contractor -> {
                handleAddClick(v.id)
            }
            R.id.imv_delete_bien, R.id.imv_delete_owner, R.id.imv_delete_locataire,
            R.id.imv_delete_mandataire -> {
                handleDeleteClick(v.id)
            }
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

    private fun handleSearchClick(id: Int) {
        val position = when (id) {
            R.id.imv_search_owner -> POSITION_FRAGMENT_PROPRIETAIRE
            R.id.imv_search_bien -> POSITION_FRAGMENT_BIENS
            R.id.imv_search_locataire -> POSITION_FRAGMENT_LOCATAIRE
            R.id.imv_search_mandataire -> POSITION_FRAGMENT_MANDATAIRE
            R.id.imv_search_agence -> POSITION_FRAGMENT_AGENCES
            R.id.imv_search_user -> POSITION_FRAGMENT_USER
            else -> return
        }
        navigateTo(R.id.go_to_search, position)
    }

    private fun handleEditClick(id: Int) {
        val (rows, label) = when (id) {
            R.id.imv_edit_owner -> Pair(this.constat.owners.map { it.name }, OWNER_LABEL)
            R.id.imv_edit_locataire -> Pair(this.constat.tenants.map { it.name }, TENANT_LABEL)
            R.id.imv_edit_mandataire -> Pair(this.constat.contractors.map { it.denomination }, CONTRACTOR_LABEL)
            R.id.imv_edit_bien -> Pair(this.constat.properties.map { it.address }, PROPERTY_LABEL)
            else -> return
        }
        whoToEdit(rows, label)
    }

    private fun handleAddClick(id: Int) {
        val position = when (id) {
            R.id.imv_add_bien -> POSITION_FRAGMENT_BIENS
            R.id.imv_add_locataire -> POSITION_FRAGMENT_LOCATAIRE
            R.id.imv_add_owner -> POSITION_FRAGMENT_PROPRIETAIRE
            R.id.imv_add_contractor -> POSITION_FRAGMENT_MANDATAIRE
            else -> return
        }
        navigateTo(R.id.go_to_add, position)
    }

    private fun handleDeleteClick(id: Int) {
        when (id) {
            R.id.imv_delete_bien -> launch { viewModel.deleteConstatPropertyCrossRefByIds() }
            R.id.imv_delete_owner -> launch { viewModel.deleteConstatOwnerCrossRefByIds() }
            R.id.imv_delete_locataire -> launch { viewModel.deleteConstatTenantCrossRefByIds() }
            R.id.imv_delete_mandataire -> launch { viewModel.deleteConstatContractorCrossRefByIds() }
            else -> return
        }
    }

    private fun navigateTo(destination: Int, position: Int) {
        val bundle = bundleOf(
            ARGS_TAB_POSITION to position,
            ARGS_CONSTAT to this.constat
        )
        findNavController().navigate(destination, bundle)
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