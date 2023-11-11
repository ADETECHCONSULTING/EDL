package fr.atraore.edl.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.EquipmentData

private val TYPE_PARENT = 1
private val TYPE_CHILD = 2

class MultiLevelAdapter(private val data: List<EquipmentData>, private val listener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // Logic to determine the view type
        // For simplicity, assuming each ParentData has only one ChildData
        return if (position % 2 == 0) TYPE_PARENT else TYPE_CHILD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PARENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item, parent, false)
                ParentViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.child_list_item, parent, false)
                ChildViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_PARENT -> {
                val parentViewHolder = holder as ParentViewHolder
                val parentData = data[position / 2]
                parentViewHolder.libelle.text = parentData.eqp
            }
            TYPE_CHILD -> {
                val childViewHolder = holder as ChildViewHolder
                val childData = data[position / 2].childData
                childViewHolder.child.text = childData.children.toString()
                childViewHolder.itemView.setOnClickListener {
                    listener(childData.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size * 2
    }
}
