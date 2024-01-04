package fr.atraore.edl.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.BaseReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.element_list_item.view.*
import kotlinx.android.synthetic.main.room_simple_list_item.view.*

class RoomSimpleAdapter : RecyclerView.Adapter<RoomSimpleAdapter.RoomSimpleViewHolder>() {

    private var data: List<BaseReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener
    var currentItemSelected: BaseReference? = null

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomSimpleViewHolder {
        return RoomSimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.room_simple_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RoomSimpleViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<BaseReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class RoomSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BaseReference) = with(itemView) {
            if (checkedPosition == -1) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                itemView.rb_room_parent.setTextColor(Color.BLACK)
            } else {
                if (checkedPosition == absoluteAdapterPosition) {
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                    itemView.rb_room_parent.setTextColor(Color.WHITE)
                } else {
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                    itemView.rb_room_parent.setTextColor(Color.BLACK)
                }
            }
            itemView.rb_room_parent.text = item.name
            itemView.setTag(this@RoomSimpleViewHolder)
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}