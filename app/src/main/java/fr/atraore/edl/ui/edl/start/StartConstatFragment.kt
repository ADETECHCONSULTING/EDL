package fr.atraore.edl.ui.edl.start

import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.databinding.StartConstatFragmentBinding
import fr.atraore.edl.ui.adapter.start.TenantInfoAdapter
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.start_constat_fragment.*

class StartConstatFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private val startViewModel: StartConstatViewModel by viewModels() {
        val edlApplication = activity?.application as EdlApplication;
        StartConstatViewModelFactory(edlApplication.constatRepository, edlApplication.tenantRepository, arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    private lateinit var tenantInfoAdapter: TenantInfoAdapter

    private lateinit var binding: StartConstatFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_constat_fragment, container, false)
        binding.constatViewModel = startViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tenantInfoAdapter = TenantInfoAdapter()
        rcv_tenant.adapter = tenantInfoAdapter
        rcv_tenant.layoutManager = LinearLayoutManager(context)

        startViewModel.constatDetail.observe(viewLifecycleOwner, Observer { constatWithDetails ->
            constatWithDetails?.let { tenantInfoAdapter.submitList(it.tenants) }
        })

        initListener()
    }

    fun initListener() {
        imv_search_bien.setOnClickListener(this)
        imv_search_owner.setOnClickListener(this)
        imv_search_locataire.setOnClickListener(this)
        imv_search_mandataire.setOnClickListener(this)
        imv_search_agence.setOnClickListener(this)

        imv_edit_owner.setOnClickListener(this)
        imv_edit_bien.setOnClickListener(this)
        imv_edit_locataire.setOnClickListener(this)
        imv_edit_mandataire.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //click on Item : search icon
            R.id.imv_search_owner -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_bien -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_locataire -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_mandataire -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE)
                findNavController().navigate(R.id.go_to_search, bundle)
            }
            R.id.imv_search_agence -> {
                val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_AGENCES)
                findNavController().navigate(R.id.go_to_search, bundle)
            }

            //click on Item : Edit icon
            R.id.imv_edit_owner -> {
                getEditableDialog(txv_owners.text as String)
            }
            R.id.imv_edit_locataire -> {
                tenantInfoAdapter.editUpdate()
                //getEditableDialog(txv_tenant.text as String)
            }
            R.id.imv_edit_mandataire -> {
                getEditableDialog(txv_contractor.text as String)
            }
            R.id.imv_edit_bien -> {
                getEditableDialog(txv_biens.text as String)
            }

            else -> {
                Toast.makeText(context, "Une erreur est survenue. Veuillez revenir au menu principal et réessayer", Toast.LENGTH_SHORT).show()
            }
        }
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
                .setPositiveButton("Renommer") { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, which ->
                    dialog.dismiss()
                }
        }

        //specify the body to inflate
        builder?.setView(viewInflated)

        builder?.show()

    }

}