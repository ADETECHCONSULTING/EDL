package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Tenant
import fr.atraore.edl.databinding.TenantItemBinding
import fr.atraore.edl.ui.edl.search.tenant.TenantSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TenantAdapter(private val tenantSearchViewModel: TenantSearchViewModel, private val constatDetails: ConstatWithDetails) : ListAdapter<Tenant, TenantAdapter.ViewHolder>(DiffTenantCallback()), CoroutineScope {
    private val TAG = TenantAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TenantItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tenant = getItem(position)
        if (constatDetails.tenants.find { it.tenantId == tenant.tenantId } !== null) {
            holder.apply {
                bind(deleteClickListener(tenant), tenant, false)
                itemView.tag = tenant
            }
        } else {
            holder.apply {
                bind(createClickListener(tenant), tenant, true)
                itemView.tag = tenant
            }
        }
    }

    private fun createClickListener(tenant: Tenant): View.OnClickListener {
        return View.OnClickListener {
            launch {
                tenantSearchViewModel.saveConstatTenant(constatDetails.constat.constatId, tenant.tenantId)
                Log.d(TAG, "Creation ConstatTenantCrossRef ${tenant} in ${constatDetails.constat.constatId}")
                constatDetails.tenants.add(tenant) //supprime de la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    private fun deleteClickListener(tenant: Tenant): View.OnClickListener {
        return View.OnClickListener {
            launch {
                tenantSearchViewModel.deleteConstatTenant(constatDetails.constat.constatId, tenant.tenantId)
                Log.d(TAG, "Creation ConstatTenantCrossRef ${tenant} in ${constatDetails.constat.constatId}")
                constatDetails.tenants.remove(tenant) //supprime de la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(
        private val binding: TenantItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Tenant,
            itemState: Boolean
        ) {
            binding.apply {
                tenantItem = item
                addClickListener = listenerProperty
                state = itemState
            }
        }
    }
}

class DiffTenantCallback : DiffUtil.ItemCallback<Tenant>() {

    override fun areItemsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
        return oldItem.tenantId == newItem.tenantId
    }

    override fun areContentsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
        return oldItem == newItem
    }


}