package fr.atraore.edl.ui.edl.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.databinding.StartConstatFragmentBinding
import fr.atraore.edl.ui.adapter.start.PrimaryInfoNoDataBindAdapter
import fr.atraore.edl.ui.formatToServerDateTimeDefaults
import fr.atraore.edl.ui.hideKeyboard
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.start_constat_fragment.*

class StartConstatFragment : Fragment(), View.OnClickListener, LifecycleObserver {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private val startViewModel: StartConstatViewModel by viewModels() {
        val edlApplication = activity?.application as EdlApplication;
        StartConstatViewModelFactory(edlApplication.constatRepository, edlApplication.tenantRepository, edlApplication.ownerRepository, edlApplication.propertyRepository, edlApplication.contractorRepository, arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    private lateinit var binding: StartConstatFragmentBinding
    private lateinit var constat: ConstatWithDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_constat_fragment, container, false)
        binding.constatViewModel = startViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startViewModel.constatDetail.observe(viewLifecycleOwner, Observer { constatWithDetails ->
            constatWithDetails?.let {
                this.constat = constatWithDetails
                startViewModel.constatHeaderInfo.value = "Constat d'état des lieux ${getConstatEtat(constatWithDetails.constat.typeConstat)} - ${constatWithDetails.constat.dateCreation.formatToServerDateTimeDefaults()}"
                configRecyclerViewsLinear(
                    rcv_tenant,
                    rcv_biens,
                    rcv_contractor,
                    rcv_owner,
                    constatWithDetails = it,
                    startConstatViewModel = startViewModel
                )

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

        imv_edit_owner.setOnClickListener(this)
        imv_edit_bien.setOnClickListener(this)
        imv_edit_locataire.setOnClickListener(this)
        imv_edit_mandataire.setOnClickListener(this)

        imv_add_bien.setOnClickListener(this)
        imv_add_contractor.setOnClickListener(this)
        imv_add_locataire.setOnClickListener(this)
        imv_add_owner.setOnClickListener(this)
    }

    /**
     * Configure les recyclerviews de façon lineaire
     */
    private fun configRecyclerViewsLinear(vararg recyclerViews: RecyclerView, constatWithDetails: ConstatWithDetails, startConstatViewModel: StartConstatViewModel) {
        recyclerViews.forEach {
            when (it.id) {
                R.id.rcv_tenant -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(constatWithDetails.tenants, startConstatViewModel)
                }
                R.id.rcv_owner -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(constatWithDetails.owners, startConstatViewModel)
                }
                R.id.rcv_biens -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(constatWithDetails.properties, startConstatViewModel)
                }
                R.id.rcv_contractor -> {
                    it.adapter = PrimaryInfoNoDataBindAdapter(constatWithDetails.contractors, startConstatViewModel)
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
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE, ARGS_CONSTAT to this.constat)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_bien -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS, ARGS_CONSTAT to this.constat)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_locataire -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE, ARGS_CONSTAT to this.constat)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_mandataire -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE, ARGS_CONSTAT to this.constat)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_agence -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_AGENCES, ARGS_CONSTAT to this.constat)
                findNavController().navigate(R.id.go_to_search, bundle)
            }

            //click on Item : Edit icon
            //click true active l'edition
            //click false sauvegarde le contenu
            R.id.imv_edit_owner -> {
                editOrSave(rcv_owner.adapter as PrimaryInfoNoDataBindAdapter)
            }
            R.id.imv_edit_locataire -> {
                editOrSave(rcv_tenant.adapter as PrimaryInfoNoDataBindAdapter)
            }
            R.id.imv_edit_mandataire -> {
                editOrSave(rcv_contractor.adapter as PrimaryInfoNoDataBindAdapter)
            }
            R.id.imv_edit_bien -> {
                editOrSave(rcv_biens.adapter as PrimaryInfoNoDataBindAdapter)
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

            else -> {
                Toast.makeText(context, "Une erreur est survenue. Veuillez revenir au menu principal et réessayer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editOrSave(primaryInfoAdapter: PrimaryInfoNoDataBindAdapter) {
        if (primaryInfoAdapter.edit) {
            if (startViewModel.constatDetail.value != null) {
                primaryInfoAdapter.saveContent()
                hideKeyboard() //utilisation de l'extension pour fermer le clavier
            }
        }

        primaryInfoAdapter.editUpdate()
    }

    fun getEditableDialog(text: String) {
        //inflate the layout for the body of the dialog
        val viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_edit, start_constat_container, false)
        //set up the input
        val input = viewInflated.findViewById<EditText>(R.id.edt_rename)
        input.setText(text)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Voulez-vous effectuer un renommage ?")
                .setPositiveButton("Renommer") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }
        }

        //specify the body to inflate
        builder?.setView(viewInflated)

        builder?.show()

    }

    fun getConstatEtat(etat: String): String {
        when (etat) {
            "E" -> {
                return "entrant"
            }
            "PE" -> {
                return "pré-état"
            }
            "S" -> {
                return "sortant"
            } else -> {
                return ""
            }
        }
    }

}