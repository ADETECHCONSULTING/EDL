package fr.atraore.edl.ui.edl.constat.second_page.groupie

import android.view.View
import androidx.compose.ui.res.colorResource
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import kotlinx.android.synthetic.main.element_list_item.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import fr.atraore.edl.data.models.Detail

var allChildItems: MutableList<View> = mutableListOf()

class ChildItem(val detail : Detail, val actionHandler: IActionHandler) : Item() {
    var rowIndex = -1

    interface IActionHandler {
        fun onLongClick(anchorView: View, detail : Detail)

        fun onSimpleClick(detail : Detail)
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_element_child.text = detail.intitule

        if (!allChildItems.contains(viewHolder.itemView)) {
            allChildItems.add(viewHolder.itemView)
        }

        viewHolder.itemView.setOnClickListener  {
            allChildItems.forEach {
                it.setBackgroundColor(viewHolder.itemView.context.resources.getColor(R.color.white))
                it.txv_element_child.setTextColor(viewHolder.itemView.context.resources.getColor(R.color.black))
            }
            it.setBackgroundColor(viewHolder.itemView.context.resources.getColor(R.color.colorPrimary))
            it.txv_element_child.setTextColor(viewHolder.itemView.context.resources.getColor(R.color.white))
            actionHandler.onSimpleClick(detail)
        }

        viewHolder.itemView.setOnLongClickListener {
            actionHandler.onLongClick(viewHolder.itemView, detail)
            true
        }

    }

    override fun getLayout(): Int {
        return R.layout.element_list_item
    }

}