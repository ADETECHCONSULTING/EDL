package fr.atraore.edl.ui.edl.search.owner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.ui.adapter.OwnerAdapter
import fr.atraore.edl.ui.edl.search.BaseFragment
import kotlinx.android.synthetic.main.owner_search_fragment.*

class OwnerSearchFragment : BaseFragment() {

    override val title: String
        get() = "Propriétaires"

    companion object {
        fun newInstance() = OwnerSearchFragment()
    }

    private val ownerSearchViewModel: OwnerSearchViewModel by viewModels {
        OwnerSearchViewModelFactory((activity?.application as EdlApplication).ownerRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.owner_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OwnerAdapter()
        rcv_owner.adapter = adapter
        rcv_owner.layoutManager = GridLayoutManager(context, 4)

        ownerSearchViewModel.allOwners.observe(viewLifecycleOwner, Observer {owners ->
            owners?.let { adapter.submitList(it) }
        })
    }

}