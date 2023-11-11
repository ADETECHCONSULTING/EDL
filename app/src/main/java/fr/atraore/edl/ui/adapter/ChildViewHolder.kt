package fr.atraore.edl.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R

class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val child: TextView = itemView.findViewById(R.id.txv_child)
}