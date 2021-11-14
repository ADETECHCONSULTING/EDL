package fr.atraore.edl.ui.edl.constat.second_page.groupie

import android.view.View
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import kotlinx.android.synthetic.main.element_list_item.view.*
import androidx.recyclerview.widget.ItemTouchHelper




class ChildItem(val elementReference : ElementReference, val actionHandler: IActionHandler) : Item() {

    interface IActionHandler {
        fun onLongClick(anchorView: View, elementReference: ElementReference)

        fun onSimpleClick(elementReference: ElementReference)
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_element_child.text = elementReference.name

        viewHolder.itemView.setOnClickListener  {
            actionHandler.onSimpleClick(elementReference)
        }

        viewHolder.itemView.setOnLongClickListener {
            actionHandler.onLongClick(viewHolder.itemView, elementReference)
            true
        }

    }

    override fun getLayout(): Int {
        return R.layout.element_list_item
    }

}