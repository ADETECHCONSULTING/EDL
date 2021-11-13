package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.databinding.PropertyItemBinding
import fr.atraore.edl.ui.edl.search.biens.PropertySearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PropertyAdapter(private val propertySearchViewModel: PropertySearchViewModel, private val constatDetails: ConstatWithDetails) : ListAdapter<Property, PropertyAdapter.ViewHolder>(DiffPropertyCallback()), CoroutineScope {
    private val TAG = PropertyAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

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
        //si bien déjà dans la liste alors event de suppression sinon event d'ajout
        if (constatDetails.properties.find { it.propertyId == property.propertyId } !== null) {
            holder.apply {
                bind(deleteConstatClickListener(property), property, false)
                itemView.tag = property
            }
        } else {
            holder.apply {
                bind(createConstatClickListener(property), property, true)
                itemView.tag = property
            }
        }
    }

    private fun createConstatClickListener(property: Property): View.OnClickListener {
        return View.OnClickListener {
            launch {
                propertySearchViewModel.saveConstatProperty(constatDetails.constat.constatId, property.propertyId)
                Log.d(TAG, "Creation ConstatPropertyCrossRef ${property} in ${constatDetails.constat.constatId}")
                constatDetails.properties.add(property) //ajout dans la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    private fun deleteConstatClickListener(property: Property): View.OnClickListener {
        return View.OnClickListener {
            launch {
                propertySearchViewModel.deleteConstatProperty(constatDetails.constat.constatId, property.propertyId)
                Log.d(TAG, "Suppression ConstatPropertyCrossRef ${property} in ${constatDetails.constat.constatId}")
                constatDetails.properties.remove(property) //supprime de la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(
        private val binding: PropertyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Property,
            itemState: Boolean
        ) {
            binding.apply {
                propertyItem = item
                addClickListener = listenerProperty
                state = itemState
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