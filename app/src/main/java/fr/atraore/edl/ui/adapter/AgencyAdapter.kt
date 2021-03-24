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
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.databinding.AgencyItemBinding
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.databinding.PropertyItemBinding

class AgencyAdapter : ListAdapter<Agency, AgencyAdapter.ViewHolder>(DiffAgencyCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AgencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agency = getItem(position)
        holder.apply {
            bind(createClickListener(agency), agency)
            itemView.tag = agency
        }
    }

    private fun createClickListener(agency: Agency): View.OnClickListener {
        //TODO insert
        return View.OnClickListener {
            Log.d("Property Adapter", "createConstatClickListener: CLICKED ${agency}",)
        }
    }

    class ViewHolder(
        private val binding: AgencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Agency
        ) {
            binding.apply {
                agencyItem = item
                addClickListener = listenerProperty
            }
        }
    }
}

private class DiffAgencyCallback : DiffUtil.ItemCallback<Agency>() {

    override fun areItemsTheSame(oldItem: Agency, newItem: Agency): Boolean {
        return oldItem.agencyId == newItem.agencyId
    }

    override fun areContentsTheSame(oldItem: Agency, newItem: Agency): Boolean {
        return oldItem == newItem
    }


}