package fr.atraore.edl.ui.edl.search.agency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.ui.adapter.AgencyAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.AGENCY_LABEL
import kotlinx.android.synthetic.main.agency_search_fragment.*

@AndroidEntryPoint
class AgencySearchFragment(private val constat: ConstatWithDetails) : BaseFragment(AGENCY_LABEL) {

    override val title: String
        get() = AGENCY_LABEL

    companion object {
        fun newInstance(constat: ConstatWithDetails) = AgencySearchFragment(constat)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private val agencyViewModel: AgencySearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agency_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AgencyAdapter(agencyViewModel, constat)
        rcv_agency.adapter = adapter
        rcv_agency.layoutManager = GridLayoutManager(context, 4)

        agencyViewModel.allAgencies.observe(viewLifecycleOwner, Observer {agencies ->
            agencies?.let { adapter.submitList(it) }
        })
    }

}