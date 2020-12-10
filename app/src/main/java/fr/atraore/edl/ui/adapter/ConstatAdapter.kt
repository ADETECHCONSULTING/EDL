package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.data.models.Constat

class ConstatAdapter: ListAdapter<Constat, ConstatAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ConstatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val constat = getItem(position)
        holder.apply {
            bind(createPdfOnClickListener(constat), constat)
            itemView.tag = constat
        }
    }

    private fun createPdfOnClickListener(constat: Constat): View.OnClickListener {
        //TODO gérer la création du pdf
        return View.OnClickListener {
            Log.d("ConstatAdapter", "createPdfOnClickListener: CLICKED")
        }
    }

    class ViewHolder(
        private val binding: ConstatItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Constat) {
            binding.apply {
                pdfClickListener = listener
                constatItem = item
            }
        }
    }
}

private class DiffCallback: DiffUtil.ItemCallback<Constat>() {

    override fun areItemsTheSame(oldItem: Constat, newItem: Constat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Constat, newItem: Constat): Boolean {
        return oldItem == newItem
    }

}