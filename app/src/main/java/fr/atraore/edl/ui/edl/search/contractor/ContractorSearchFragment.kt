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
import kotlinx.android.synthetic.main.contractor_search_fragment.*

class ContractorSearchFragment : BaseFragment() {

    override val title: String
        get() = "Mandataires"

    companion object {
        fun newInstance() = ContractorSearchFragment()
    }

    private val contractorViewModel: ContractorSearchViewModel by viewModels {
        ContractorSearchViewModelFactory((activity?.application as EdlApplication).contractorRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contractor_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ContractorAdapter()
        rcv_contractor.adapter = adapter
        rcv_contractor.layoutManager = GridLayoutManager(context, 3)

        contractorViewModel.allContractors.observe(viewLifecycleOwner, Observer { contractors ->
            contractors?.let { adapter.submitList(it) }
        })
    }

}