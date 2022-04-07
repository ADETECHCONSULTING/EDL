package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.ElementReference
import kotlinx.android.synthetic.main.element_grid_item.view.*
import kotlinx.android.synthetic.main.element_list_item.view.*
import java.util.*

class ElementGridAdapter : RecyclerView.Adapter<ElementGridAdapter.ElementGridViewHolder>() {

    private var data: List<ElementReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementGridViewHolder {
        return ElementGridViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.element_grid_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ElementGridViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<ElementReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class ElementGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ElementReference) = with(itemView) {

            itemView.checkbox_element.text = item.name
            itemView.checkbox_element.setTag(this@ElementGridViewHolder)
            itemView.checkbox_element.setOnClickListener(mOnItemClickListener)
        }
    }
}