package fr.atraore.edl.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R

class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val libelle: TextView = itemView.findViewById(R.id.txv_room_parent)
}


