package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.android.synthetic.main.child_list_item.view.*
import kotlinx.android.synthetic.main.element_list_item.view.*
import kotlinx.android.synthetic.main.room_simple_list_item.view.*

class DetailSimpleAdapter : RecyclerView.Adapter<DetailSimpleAdapter.DetailSimpleViewHolder>() {

    private var data: List<Detail> = ArrayList()
    private lateinit var mOnItemClickListener: View.OnClickListener
    var currentItemSelected: ElementReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSimpleViewHolder {
        return DetailSimpleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.child_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DetailSimpleViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Detail>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        this.mOnItemClickListener = itemClickListener
    }

    inner class DetailSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Detail) = with(itemView) {
            itemView.txv_child.text = item.intitule
            itemView.setTag(this@DetailSimpleViewHolder)
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}