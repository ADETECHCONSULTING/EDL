package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.KeyReference
import kotlinx.android.synthetic.main.element_grid_item.view.*

class KeysGridAdapter : RecyclerView.Adapter<KeysGridAdapter.KeyGridViewHolder>() {

    private var data: List<KeyReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyGridViewHolder {
        return KeyGridViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.element_grid_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: KeyGridViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<KeyReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class KeyGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: KeyReference) = with(itemView) {
            itemView.checkbox_element.text = item.name
            itemView.checkbox_element.setTag(this@KeyGridViewHolder)
            itemView.checkbox_element.setOnClickListener(mOnItemClickListener)

            if (item.actif) {
                itemView.checkbox_element.background = ResourcesCompat.getDrawable(resources, R.drawable.button_solid_shape, null)
                itemView.checkbox_element.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                itemView.checkbox_element.background = ResourcesCompat.getDrawable(resources, R.drawable.button_outline_shape_2, null)
                itemView.checkbox_element.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreyjoy))
            }
        }
    }
}