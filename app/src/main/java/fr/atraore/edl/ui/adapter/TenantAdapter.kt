package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.databinding.PropertyItemBinding
import fr.atraore.edl.databinding.TenantItemBinding

class TenantAdapter : ListAdapter<Tenant, TenantAdapter.ViewHolder>(DiffTenantCallback()) {

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
        holder.apply {
            bind(createClickListener(tenant), tenant)
            itemView.tag = tenant
        }
    }

    private fun createClickListener(tenant: Tenant): View.OnClickListener {
        //TODO insert
        return View.OnClickListener {
            Log.d("Property Adapter", "createConstatClickListener: CLICKED")
        }
    }

    class ViewHolder(
        private val binding: TenantItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Tenant
        ) {
            binding.apply {
                tenantItem = item
                addClickListener = listenerProperty
            }
        }
    }
}

private class DiffTenantCallback : DiffUtil.ItemCallback<Tenant>() {

    override fun areItemsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
        return oldItem.tenantId == newItem.tenantId
    }

    override fun areContentsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
        return oldItem == newItem
    }


}