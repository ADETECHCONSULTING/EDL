package fr.atraore.edl.ui.edl.search.tenant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.ui.adapter.TenantAdapter
import fr.atraore.edl.ui.edl.search.BaseFragment
import kotlinx.android.synthetic.main.tenant_search_fragment.*

class TenantSearchFragment : BaseFragment() {

    override val title: String
        get() = "Locataire"

    companion object {
        fun newInstance() = TenantSearchFragment()
    }

    private val tenantViewModel: TenantSearchViewModel by viewModels {
        TenantSearchViewModelFactory((activity?.application as EdlApplication).tenantRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tenant_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = TenantAdapter()
        rcv_tenant.adapter = adapter
        rcv_tenant.layoutManager = GridLayoutManager(context, 4)

        tenantViewModel.allTenants.observe(viewLifecycleOwner, Observer {tenants ->
            tenants?.let { adapter.submitList(it) }
        })
    }

}