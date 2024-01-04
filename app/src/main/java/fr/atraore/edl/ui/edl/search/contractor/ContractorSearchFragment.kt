package fr.atraore.edl.ui.edl.search.contractor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.ui.adapter.ContractorAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.CONTRACTOR_LABEL
import kotlinx.android.synthetic.main.fragment_contractor_search.*

@AndroidEntryPoint
class ContractorSearchFragment(private val constat: ConstatWithDetails) : BaseFragment(CONTRACTOR_LABEL) {

    override val title: String
        get() = CONTRACTOR_LABEL

    companion object {
        fun newInstance(constat: ConstatWithDetails) = ContractorSearchFragment(constat)
    }

    override fun goNext() {
        return
    }

    private val contractorViewModel: ContractorSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contractor_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ContractorAdapter(contractorViewModel, constat)
        rcv_contractor.adapter = adapter
        rcv_contractor.layoutManager = GridLayoutManager(context, 4)

        contractorViewModel.allContractors.observe(viewLifecycleOwner, Observer { contractors ->
            contractors?.let { adapter.submitList(it) }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_previous)?.isVisible = true
    }

}