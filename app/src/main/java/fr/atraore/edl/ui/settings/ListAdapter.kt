package fr.atraore.edl.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import kotlinx.android.synthetic.main.eqp_list_item.view.txv_eqp

class ListAdapter(private var list: List<String>, private val level: Int, private val listener: OnLevelsItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private var selectedItem = -1
    private val originalList = list

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if ((itemView.parent as RecyclerView).isEnabled) {
                    notifyItemChanged(selectedItem)
                    selectedItem = layoutPosition
                    notifyItemChanged(selectedItem)

                    if (level == 1) {
                        listener.onFirstLevelItemClick()
                    } else if (level == 2) {
                        listener.onSecondLevelItemClick()
                    } else {
                        listener.onThirdLevelItemClick()
                    }
                } else {
                    if (level == 2) {
                        Toast.makeText(itemView.context, "Veuillez sélectionner un élément de niveau 1", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(itemView.context, "Veuillez sélectionner un élément de niveau 2", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        fun bind(item: String) {
            itemView.txv_eqp.text = item
            if (selectedItem == layoutPosition) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.eqp_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<String>) {
        list = newList
        notifyDataSetChanged()
    }

    fun getSelectedItem(): String {
        return list[selectedItem]
    }
}