package fr.atraore.edl.ui.edl.search.contractor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.ui.adapter.ContractorAdapter
import fr.atraore.edl.ui.edl.search.BaseFragment
import fr.atraore.edl.ui.edl.search.biens.PropertySearchFragment
import kotlinx.android.synthetic.main.contractor_search_fragment.*

class ContractorSearchFragment(private val constatId: String) : BaseFragment() {

    override val title: String
        get() = "Mandataires"

    companion object {
        fun newInstance(constatId: String) = ContractorSearchFragment(constatId)
    }

    private val contractorViewModel: ContractorSearchViewModel by viewModels {
        val edlApplication = (activity?.application as EdlApplication)
        ContractorSearchViewModelFactory(edlApplication.contractorRepository, edlApplication.constatRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contractor_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ContractorAdapter(contractorViewModel, constatId)
        rcv_contractor.adapter = adapter
        rcv_contractor.layoutManager = GridLayoutManager(context, 4)

        contractorViewModel.allContractors.observe(viewLifecycleOwner, Observer { contractors ->
            contractors?.let { adapter.submitList(it) }
        })
    }

}