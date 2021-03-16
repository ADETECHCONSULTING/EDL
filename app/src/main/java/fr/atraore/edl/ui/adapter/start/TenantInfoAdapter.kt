package fr.atraore.edl.ui.adapter.start

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.databinding.TenantStartItemBinding
import fr.atraore.edl.ui.adapter.DiffTenantCallback

class TenantInfoAdapter : ListAdapter<Tenant, TenantInfoAdapter.ViewHolder>(DiffTenantCallback()) {

    var edit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TenantStartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tenant = getItem(position)
        holder.apply {
            bind(editClickListener(tenant), tenant, edit)
            itemView.tag = tenant
        }
    }

    private fun editClickListener(tenant: Tenant): View.OnClickListener {
        //TODO insert
        return View.OnClickListener {
            Log.d("Tenant Info Adapter", "editClickListener: CLICKED")
        }
    }

    fun editUpdate() {
        edit = !edit
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: TenantStartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Tenant,
            edit: Boolean
        ) {
            binding.apply {
                tenantItem = item
                editClickListener = listenerProperty
                editItem = edit
            }
        }
    }
}