package fr.atraore.edl.ui.edl.search.biens

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class PropertySearchFragment : BaseFragment() {

    override val title: String
        get() = "Biens"

    companion object {
        fun newInstance() = PropertySearchFragment()
    }

    private lateinit var viewModel: PropertySearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.property_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PropertySearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}