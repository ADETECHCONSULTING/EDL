package fr.atraore.edl.ui.edl.search.agency

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class AgencySearchFragment : BaseFragment() {

    override val title: String
        get() = "Agences"

    companion object {
        fun newInstance() = AgencySearchFragment()
    }

    private lateinit var viewModel: AgencySearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agency_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgencySearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}