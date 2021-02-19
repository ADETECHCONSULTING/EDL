package fr.atraore.edl.ui.edl.search.agency

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
import fr.atraore.edl.ui.adapter.AgencyAdapter
import fr.atraore.edl.ui.edl.search.BaseFragment
import kotlinx.android.synthetic.main.agency_search_fragment.*

class AgencySearchFragment : BaseFragment() {

    override val title: String
        get() = "Agences"

    companion object {
        fun newInstance() = AgencySearchFragment()
    }

    private val agencyViewModel: AgencySearchViewModel by viewModels {
        AgencySearchViewModelFactory((activity?.application as EdlApplication).agencyRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agency_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = AgencyAdapter()
        rcv_agency.adapter = adapter
        rcv_agency.layoutManager = GridLayoutManager(context, 4)

        agencyViewModel.allAgencies.observe(viewLifecycleOwner, Observer {agencies ->
            agencies?.let { adapter.submitList(it) }
        })
    }

}