package fr.atraore.edl.ui.edl.constat.second_page.groupie

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import kotlinx.android.synthetic.main.element_list_item.view.*

class ChildItem( val elementChild : ElementReference) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_element_child.text = elementChild.name

    }
    override fun getLayout(): Int {
        return R.layout.element_list_item
    }

}