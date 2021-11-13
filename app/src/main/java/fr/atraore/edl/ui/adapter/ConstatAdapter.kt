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
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.data.models.data.ConstatWithDetails

class ConstatAdapter : ListAdapter<ConstatWithDetails, ConstatAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ConstatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val constat = getItem(position)
        holder.apply {
            bind(createPdfOnClickListener(constat), createConstatClickListener(constat), constat)
            itemView.tag = constat
        }
    }

    private fun createPdfOnClickListener(constat: ConstatWithDetails): View.OnClickListener {
        //TODO gérer la création du pdf
        return View.OnClickListener {
            Log.d("ConstatAdapter", "createPdfOnClickListener: CLICKED")
        }
    }

    private fun createConstatClickListener(constat: ConstatWithDetails): View.OnClickListener {
        val bundle = bundleOf("constatId" to constat.constat.constatId)
        //TODO add UUID to constat id
        return Navigation.createNavigateOnClickListener(R.id.go_to_start, bundle)
    }

    class ViewHolder(
        private val binding: ConstatItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerPdf: View.OnClickListener,
            listenerConstat: View.OnClickListener,
            item: ConstatWithDetails
        ) {
            binding.apply {
                pdfClickListener = listenerPdf
                constatItem = item
                constatClickListener = listenerConstat
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<ConstatWithDetails>() {

    override fun areItemsTheSame(oldItem: ConstatWithDetails, newItem: ConstatWithDetails): Boolean {
        return oldItem.constat.constatId == newItem.constat.constatId
    }

    override fun areContentsTheSame(oldItem: ConstatWithDetails, newItem: ConstatWithDetails): Boolean {
        return oldItem == newItem
    }

}