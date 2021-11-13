package fr.atraore.edl.ui.edl.search.owner

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
import fr.atraore.edl.ui.adapter.OwnerAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.OWNER_LABEL
import kotlinx.android.synthetic.main.owner_search_fragment.*

@AndroidEntryPoint
class OwnerSearchFragment(private val constat: ConstatWithDetails) : BaseFragment(OWNER_LABEL) {

    override val title: String
        get() = OWNER_LABEL

    companion object {
        fun newInstance(constat: ConstatWithDetails) = OwnerSearchFragment(constat)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private val ownerSearchViewModel: OwnerSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.owner_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OwnerAdapter(ownerSearchViewModel, constat)
        rcv_owner.adapter = adapter
        rcv_owner.layoutManager = GridLayoutManager(context, 4)

        ownerSearchViewModel.allOwners.observe(viewLifecycleOwner, Observer {owners ->
            owners?.let { adapter.submitList(it) }
        })
    }

}