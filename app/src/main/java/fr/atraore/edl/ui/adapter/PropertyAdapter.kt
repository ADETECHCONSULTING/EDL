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
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.databinding.PropertyItemBinding

class PropertyAdapter : ListAdapter<Property, PropertyAdapter.ViewHolder>(DiffPropertyCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PropertyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = getItem(position)
        holder.apply {
            bind(createConstatClickListener(property), property)
            itemView.tag = property
        }
    }

    private fun createConstatClickListener(property: Property): View.OnClickListener {
        //TODO insert
        return View.OnClickListener {
            Log.d("Property Adapter", "createConstatClickListener: CLICKED")
        }
    }

    class ViewHolder(
        private val binding: PropertyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Property
        ) {
            binding.apply {
                propertyItem = item
                addClickListener = listenerProperty
            }
        }
    }
}

private class DiffPropertyCallback : DiffUtil.ItemCallback<Property>() {

    override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
        return oldItem.propertyId == newItem.propertyId
    }

    override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
        return oldItem == newItem
    }


}