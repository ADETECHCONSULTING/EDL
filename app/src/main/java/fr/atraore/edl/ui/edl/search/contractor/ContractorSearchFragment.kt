package fr.atraore.edl.ui.edl.search.contractor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class ContractorSearchFragment : BaseFragment() {

    override val title: String
        get() = "Mandataires"

    companion object {
        fun newInstance() = ContractorSearchFragment()
    }

    private lateinit var viewModel: ContractorSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contractor_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContractorSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}