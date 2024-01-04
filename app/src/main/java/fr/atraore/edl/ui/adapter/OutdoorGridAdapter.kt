package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.OutdoorEquipementReference
import kotlinx.android.synthetic.main.element_grid_item.view.*

class OutdoorGridAdapter : RecyclerView.Adapter<OutdoorGridAdapter.OutdoorGridViewHolder>() {

    private var data: List<OutdoorEquipementReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutdoorGridViewHolder {
        return OutdoorGridViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.element_grid_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: OutdoorGridViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<OutdoorEquipementReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class OutdoorGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: OutdoorEquipementReference) = with(itemView) {
            itemView.checkbox_element.text = item.name
            itemView.checkbox_element.setTag(this@OutdoorGridViewHolder)
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