package fr.atraore.edl.ui.edl.search.biens

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
import fr.atraore.edl.ui.adapter.PropertyAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.PROPERTY_LABEL
import kotlinx.android.synthetic.main.property_search_fragment.*

@AndroidEntryPoint
class PropertySearchFragment(private val constat: ConstatWithDetails) : BaseFragment(PROPERTY_LABEL) {

    override val title: String
        get() = PROPERTY_LABEL

    companion object {
        fun newInstance(constat: ConstatWithDetails) = PropertySearchFragment(constat)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private val propertySearchViewModel: PropertySearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.property_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PropertyAdapter(propertySearchViewModel, constat)
        rcv_property.adapter = adapter
        rcv_property.layoutManager = GridLayoutManager(context, 4)

        propertySearchViewModel.allProperties.observe(viewLifecycleOwner, Observer { properties ->
            properties?.let { adapter.submitList(it) }
        })
    }

}