package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.databinding.OwnerItemBinding

class OwnerAdapter : ListAdapter<Owner, OwnerAdapter.ViewHolder>(DiffOwnerCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OwnerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val owner = getItem(position)
        holder.apply {
            bind(createOwnerClickListener(owner), owner)
            itemView.tag = owner
        }
    }

    private fun createOwnerClickListener(owner: Owner): View.OnClickListener {
        //TODO insert
        return View.OnClickListener {
            Log.d("Property Adapter", "createConstatClickListener: CLICKED")
        }
    }

    class ViewHolder(
        private val binding: OwnerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Owner
        ) {
            binding.apply {
                ownerItem = item
                addClickListener = listenerProperty
            }
        }
    }
}

private class DiffOwnerCallback : DiffUtil.ItemCallback<Owner>() {

    override fun areItemsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem.ownerId == newItem.ownerId
    }

    override fun areContentsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem == newItem
    }


}