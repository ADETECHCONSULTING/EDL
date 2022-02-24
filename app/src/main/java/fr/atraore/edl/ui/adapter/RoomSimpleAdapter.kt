package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.room_list_item.view.*
import java.util.*

class RoomSimpleAdapter : RecyclerView.Adapter<RoomSimpleAdapter.RoomSimpleViewHolder>() {

    private var data: List<RoomReference> = ArrayList()

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

    class RoomSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RoomReference) = with(itemView) {

            itemView.txv_room_parent.text = item.name

            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}