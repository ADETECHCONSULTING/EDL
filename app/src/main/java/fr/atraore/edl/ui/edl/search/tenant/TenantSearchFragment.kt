package fr.atraore.edl.ui.edl.search.tenant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.ui.adapter.TenantAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import kotlinx.android.synthetic.main.tenant_search_fragment.*

@AndroidEntryPoint
class TenantSearchFragment(private val constat: ConstatWithDetails) : BaseFragment<Tenant>() {

    override val title: String
        get() = "Locataire"

    companion object {
        fun newInstance(constat: ConstatWithDetails) = TenantSearchFragment(constat)
    }

    private val tenantViewModel: TenantSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tenant_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TenantAdapter(tenantViewModel, constat)
        rcv_tenant.adapter = adapter
        rcv_tenant.layoutManager = GridLayoutManager(context, 4)

        tenantViewModel.allTenants.observe(viewLifecycleOwner, Observer { tenants ->
            tenants?.let { adapter.submitList(it) }
        })
    }

}