package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Agency
import fr.atraore.edl.databinding.AgencyItemBinding
import fr.atraore.edl.ui.edl.search.agency.AgencySearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AgencyAdapter(private val agencySearchViewModel: AgencySearchViewModel, private val constatDetails: ConstatWithDetails) : ListAdapter<Agency, AgencyAdapter.ViewHolder>(DiffAgencyCallback()), CoroutineScope {
    private val TAG = AgencyAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

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
        return View.OnClickListener {
            launch {
                if (constatDetails.agency != null) {
                    agencySearchViewModel.updateExistingAgency(constatDetails.constat, agency)
                    Log.d(TAG, "Update agence ${agency} in ${constatDetails.constat.constatId}")
                } else {
                    agencySearchViewModel.save(constatDetails.constat, agency)
                    Log.d(TAG, "Ajout agence ${agency} in ${constatDetails.constat.constatId}")
                }
            }
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