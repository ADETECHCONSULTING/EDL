package fr.atraore.edl.ui.edl.search.agency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.ui.adapter.AgencyAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import kotlinx.android.synthetic.main.agency_search_fragment.*

class AgencySearchFragment(private val constat: ConstatWithDetails) : BaseFragment<Agency>() {

    override val title: String
        get() = "Agences"

    companion object {
        fun newInstance(constat: ConstatWithDetails) = AgencySearchFragment(constat)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AgencyAdapter()
        rcv_agency.adapter = adapter
        rcv_agency.layoutManager = GridLayoutManager(context, 4)

        agencyViewModel.allAgencies.observe(viewLifecycleOwner, Observer {agencies ->
            agencies?.let { adapter.submitList(it) }
        })
    }

}