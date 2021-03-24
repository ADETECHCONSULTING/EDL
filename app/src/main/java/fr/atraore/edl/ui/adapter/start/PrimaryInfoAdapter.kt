package fr.atraore.edl.ui.adapter.start

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.dao.BaseDao
import fr.atraore.edl.data.models.PrimaryInfo


/*
class PrimaryInfoAdapter : ListAdapter<PrimaryInfo, PrimaryInfoAdapter.ViewHolder>(DiffTenantInfoCallback()) {
    private val TAG: String? = PrimaryInfoAdapter::class.simpleName

    var edit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PrimaryInfoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPrimary = getItem(position)
        holder.apply {
            bind(editClickListener(itemPrimary), itemPrimary, edit)
            itemView.tag = itemPrimary
        }
    }

    private fun editClickListener(tenant: PrimaryInfo): View.OnClickListener {
        //TODO update
        return View.OnClickListener {
            Log.d(TAG, "editClickListener: CLICKED")
        }
    }

    fun saveContent(baseEntities: PrimaryInfo) {
        Log.d(TAG, "sauvegarde de l'entity")

    }

    fun editUpdate() {
        edit = !edit
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: PrimaryInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: PrimaryInfo,
            edit: Boolean
        ) {
            binding.apply {
                itemPrimary = item
                editClickListener = listenerProperty
                editItem = edit
            }
        }
    }
}

class DiffTenantInfoCallback : DiffUtil.ItemCallback<PrimaryInfo>() {

    override fun areItemsTheSame(oldItem: PrimaryInfo, newItem: PrimaryInfo): Boolean {
        return oldItem.equals(newItem.primaryInfo())
    }

    override fun areContentsTheSame(oldItem: PrimaryInfo, newItem: PrimaryInfo): Boolean {
        return oldItem.primaryInfo() == newItem.primaryInfo()
    }

}
 */