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
import fr.atraore.edl.ui.edl.BaseFragment
import kotlinx.android.synthetic.main.property_search_fragment.*

class PropertySearchFragment(private val constatId: String) : BaseFragment() {

    override val title: String
        get() = "Biens"

    companion object {
        fun newInstance(constatId: String) = PropertySearchFragment(constatId)
    }

    private val propertySearchViewModel: PropertySearchViewModel by viewModels {
        val edlApplication = (activity?.application as EdlApplication)
        PropertySearchViewModelFactory(edlApplication.propertyRepository, edlApplication.constatRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.property_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PropertyAdapter(propertySearchViewModel, constatId)
        rcv_property.adapter = adapter
        rcv_property.layoutManager = GridLayoutManager(context, 4)

        propertySearchViewModel.allProperties.observe(viewLifecycleOwner, Observer { properties ->
            properties?.let { adapter.submitList(it) }
        })
    }

}