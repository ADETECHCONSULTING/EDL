package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Contractor
import fr.atraore.edl.databinding.ContractorItemBinding
import fr.atraore.edl.ui.edl.search.contractor.ContractorSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ContractorAdapter(private val contractorSearchViewModel: ContractorSearchViewModel, private val constatDetails: ConstatWithDetails) : ListAdapter<Contractor, ContractorAdapter.ViewHolder>(DiffContractorCallback()), CoroutineScope {
    private val TAG = ContractorAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContractorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contractor = getItem(position)
        if (constatDetails.contractors.find { it.contractorId == contractor.contractorId } !== null) {
            holder.apply {
                bind(deleteClickListener(contractor), contractor, false)
                itemView.tag = contractor
            }
        } else {
            holder.apply {
                bind(createClickListener(contractor), contractor, true)
                itemView.tag = contractor
            }
        }
    }

    private fun createClickListener(contractor: Contractor): View.OnClickListener {
        return View.OnClickListener {
            launch {
                contractorSearchViewModel.saveConstatContractor(constatDetails.constat.constatId, contractor.contractorId)
                Log.d(TAG, "Creation ConstatContractorCrossRef ${contractor} in ${constatDetails.constat.constatId}")
                constatDetails.contractors.add(contractor) //ajout dans la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    private fun deleteClickListener(contractor: Contractor): View.OnClickListener {
        return View.OnClickListener {
            launch {
                contractorSearchViewModel.deleteConstatContractor(constatDetails.constat.constatId, contractor.contractorId)
                Log.d(TAG, "Creation ConstatContractorCrossRef ${contractor} in ${constatDetails.constat.constatId}")
                constatDetails.contractors.remove(contractor) //supprime de la liste pour que l'adapter refresh l'UI à jour
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(
        private val binding: ContractorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Contractor,
            itemState: Boolean
        ) {
            binding.apply {
                contractorItem = item
                addClickListener = listenerProperty
                state = itemState
            }
        }
    }
}

private class DiffContractorCallback : DiffUtil.ItemCallback<Contractor>() {

    override fun areItemsTheSame(oldItem: Contractor, newItem: Contractor): Boolean {
        return oldItem.contractorId == newItem.contractorId
    }

    override fun areContentsTheSame(oldItem: Contractor, newItem: Contractor): Boolean {
        return oldItem == newItem
    }


}