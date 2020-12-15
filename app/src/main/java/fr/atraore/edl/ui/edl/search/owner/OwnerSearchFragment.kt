package fr.atraore.edl.ui.edl.search.owner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class OwnerSearchFragment : BaseFragment() {

    override val title: String
        get() = "Propriétaires"

    companion object {
        fun newInstance() = OwnerSearchFragment()
    }

    private lateinit var viewModel: OwnerSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.owner_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OwnerSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}