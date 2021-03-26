package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.databinding.PropertyItemBinding
import fr.atraore.edl.ui.edl.search.biens.PropertySearchViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PropertyAdapter(private val propertySearchViewModel: PropertySearchViewModel, private val constatId: String) : ListAdapter<Property, PropertyAdapter.ViewHolder>(DiffPropertyCallback()) {
    private val TAG = PropertyAdapter::class.simpleName

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
        return View.OnClickListener {
            Log.d(TAG, "Creation ConstatPropertyCrossRef ${property} in ${constatId}")
            GlobalScope.launch {
                propertySearchViewModel.saveConstatProperty(constatId, property.propertyId)
            }
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