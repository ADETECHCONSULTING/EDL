package fr.atraore.edl.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import fr.atraore.edl.R
import fr.atraore.edl.databinding.ConstatItemBinding
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.ui.pdf.PdfConstatCreatorActivity
import kotlinx.coroutines.launch

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
            bind(createPdfOnClickListener(constat, holder.itemView.context), createConstatClickListener(constat), constat)
            itemView.tag = constat
        }
    }

    private fun createPdfOnClickListener(constat: ConstatWithDetails, context: Context): View.OnClickListener {
        return View.OnClickListener {
            MaterialDialog(context).show {
                title(R.string.show_pdf_title)
                message(R.string.show_pdf_content)
                positiveButton(R.string.done) {
                    val intent = Intent(context, PdfConstatCreatorActivity::class.java)
                    intent.putExtra("constatId", constat.constat.constatId)
                    context.startActivity(intent)
                }
                negativeButton(R.string.cancel_label) {
                    dismiss()
                }
            }
        }
    }

    private fun createConstatClickListener(constat: ConstatWithDetails): View.OnClickListener {
        val bundle = bundleOf("constatId" to constat.constat.constatId)
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