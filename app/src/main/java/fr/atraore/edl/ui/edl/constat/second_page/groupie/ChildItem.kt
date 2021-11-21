package fr.atraore.edl.ui.edl.constat.second_page.groupie

import android.view.View
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import kotlinx.android.synthetic.main.element_list_item.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import fr.atraore.edl.data.models.Detail


class ChildItem(val detail : Detail, val actionHandler: IActionHandler) : Item() {

    interface IActionHandler {
        fun onLongClick(anchorView: View, detail : Detail)

        fun onSimpleClick(detail : Detail)
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_element_child.text = detail.intitule

        viewHolder.itemView.setOnClickListener  {
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