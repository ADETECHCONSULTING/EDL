package fr.atraore.edl.ui.edl.constat.second_page.groupie

import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import fr.atraore.edl.R
import fr.atraore.edl.data.models.RoomReference
import kotlinx.android.synthetic.main.room_list_item.view.*

class ParentItem(var roomParent: RoomReference) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.txv_room_parent.text = roomParent.name
        viewHolder.itemView.setOnClickListener {
            expandableGroup.onToggleExpanded()
        }
    }

    override fun getLayout() = R.layout.room_list_item

}