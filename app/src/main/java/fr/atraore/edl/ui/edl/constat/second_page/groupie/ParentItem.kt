package fr.atraore.edl.ui.edl.constat.second_page.groupie

import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import fr.atraore.edl.R
import fr.atraore.edl.data.models.RoomReference
import kotlinx.android.synthetic.main.room_list_item.view.*

class ParentItem(var roomParent: RoomReference, val actionHandler: IActionHandler) : Item(), ExpandableItem {

    interface IActionHandler {
        fun onSimpleClick(roomParent: RoomReference)
    }

    private lateinit var expandableGroup: ExpandableGroup
    lateinit var childItems: List<ChildItem>

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_room_parent.text = roomParent.name
        viewHolder.itemView.setOnClickListener {
            expandableGroup.onToggleExpanded()
            actionHandler.onSimpleClick(roomParent)
        }
    }

    override fun getLayout() = R.layout.room_list_item

}