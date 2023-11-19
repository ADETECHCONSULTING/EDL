package fr.atraore.edl.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.TreeNode
import fr.atraore.edl.utils.OnTreeNodeClickListener

class TreeNodeAdapter(private val data: List<TreeNode>, private val level: Int = 0,
                      private val listener: OnTreeNodeClickListener
) : RecyclerView.Adapter<TreeNodeAdapter.TreeNodeViewHolder>() {

    private val expandedStateMap = mutableMapOf<Int, Boolean>()
    private val colorByLevel = listOf("#DDEA5D", "#DCE487", "#E2E6BF", "#FFFFFFFF")

    inner class TreeNodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_view_item_name)
        private val childRecyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view_child)
        private val layoutBackground: View = itemView.findViewById(R.id.layout_background)

        init {
            textView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val node = data[position]
                    if (node.children.isEmpty()) {
                        listener.onNodeClicked(node.id, node.name)
                    } else {
                        // If there are children, toggle the expanded state
                        val isExpanded = expandedStateMap[position] ?: false
                        expandedStateMap[position] = !isExpanded
                        notifyItemChanged(position)
                    }
                }
            }
        }

        fun bind(node: TreeNode, isExpanded: Boolean) {
            textView.text = node.name
            layoutBackground.setBackgroundColor(Color.parseColor(colorByLevel[level]))

            // Setup child RecyclerView only if the item is expanded
            if (isExpanded && node.children.isNotEmpty()) {
                val childAdapter = TreeNodeAdapter(node.children, level + 1, listener)
                childRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                childRecyclerView.adapter = childAdapter
                childRecyclerView.visibility = View.VISIBLE
            } else {
                childRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeNodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tree_node, parent, false)
        return TreeNodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreeNodeViewHolder, position: Int) {
        val isExpanded = expandedStateMap[position] ?: false
        holder.bind(data[position], isExpanded)
    }

    override fun getItemCount(): Int = data.size
}
