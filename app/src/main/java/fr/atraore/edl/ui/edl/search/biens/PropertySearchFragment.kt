package fr.atraore.edl.ui.edl.search.biens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.ui.adapter.PropertyAdapter
import fr.atraore.edl.ui.edl.search.BaseFragment
import kotlinx.android.synthetic.main.property_search_fragment.*

class PropertySearchFragment : BaseFragment() {

    override val title: String
        get() = "Biens"

    companion object {
        fun newInstance() = PropertySearchFragment()
    }

    private val propertySearchViewModel: PropertySearchViewModel by viewModels {
        PropertySearchViewModelFactory((activity?.application as EdlApplication).propertyRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.property_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = PropertyAdapter()
        rcv_property.adapter = adapter
        rcv_property.layoutManager = GridLayoutManager(context, 4)

        propertySearchViewModel.allProperties.observe(viewLifecycleOwner, Observer { properties ->
            properties?.let { adapter.submitList(it) }
        })
    }

}