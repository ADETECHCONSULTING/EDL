package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.KeyReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.room_simple_list_item.view.*

class KeySimpleAdapter : RecyclerView.Adapter<KeySimpleAdapter.KeySimpleViewHolder>() {

    private var data: List<KeyReference> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener
    var currentItemSelected: KeyReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeySimpleViewHolder {
        return KeySimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.room_simple_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: KeySimpleViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<KeyReference>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class KeySimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: KeyReference) = with(itemView) {
            itemView.rb_room_parent.text = item.name
            itemView.setTag(this@KeySimpleViewHolder)
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}