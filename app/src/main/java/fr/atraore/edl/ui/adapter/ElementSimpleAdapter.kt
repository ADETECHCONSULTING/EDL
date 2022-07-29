package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.element_list_item.view.*
import kotlinx.android.synthetic.main.room_simple_list_item.view.*

class ElementSimpleAdapter : RecyclerView.Adapter<ElementSimpleAdapter.ElementSimpleViewHolder>() {

    private var data: List<ElementReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener
    var currentItemSelected: ElementReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementSimpleViewHolder {
        return ElementSimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.element_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ElementSimpleViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<ElementReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class ElementSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ElementReference) = with(itemView) {
            itemView.txv_element.text = item.name
            itemView.setTag(this@ElementSimpleViewHolder)
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}