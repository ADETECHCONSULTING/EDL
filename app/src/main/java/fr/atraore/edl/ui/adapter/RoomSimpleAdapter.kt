package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.room_simple_list_item.view.*

class RoomSimpleAdapter : RecyclerView.Adapter<RoomSimpleAdapter.RoomSimpleViewHolder>() {

    private var data: List<RoomReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener
    var currentItemSelected: RoomReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomSimpleViewHolder {
        return RoomSimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.room_simple_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RoomSimpleViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<RoomReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class RoomSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RoomReference) = with(itemView) {
            itemView.rb_room_parent.text = item.name
            itemView.setTag(this@RoomSimpleViewHolder)
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}