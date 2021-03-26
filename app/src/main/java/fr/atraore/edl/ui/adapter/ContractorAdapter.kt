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
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.databinding.ContractorItemBinding
import fr.atraore.edl.databinding.PropertyItemBinding
import fr.atraore.edl.ui.edl.search.biens.PropertySearchViewModel
import fr.atraore.edl.ui.edl.search.contractor.ContractorSearchViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContractorAdapter(private val contractorSearchViewModel: ContractorSearchViewModel, private val constatId: String) : ListAdapter<Contractor, ContractorAdapter.ViewHolder>(DiffContractorCallback()) {
    private val TAG = ContractorAdapter::class.simpleName

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
        holder.apply {
            bind(createClickListener(contractor), contractor)
            itemView.tag = contractor
        }
    }

    private fun createClickListener(contractor: Contractor): View.OnClickListener {
        return View.OnClickListener {
            Log.d(TAG, "Creation ConstatContractorCrossRef ${contractor} in ${constatId}")
            GlobalScope.launch {
                contractorSearchViewModel.saveConstatContractor(constatId, contractor.contractorId)
            }
        }
    }

    class ViewHolder(
        private val binding: ContractorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Contractor
        ) {
            binding.apply {
                contractorItem = item
                addClickListener = listenerProperty
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