package fr.atraore.edl.data.models.expandable

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.IExpandable
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem
import com.mikepenz.fastadapter.ui.utils.StringHolder
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Detail

class SimpleSubItem : AbstractExpandableItem<SimpleSubItem.ViewHolder>(), IExpandable<SimpleSubItem.ViewHolder> {

    var header: Detail? = null

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_sub_item_id

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    override val layoutRes: Int
        get() = R.layout.element_list_item

    fun withHeader(detail: Detail): SimpleSubItem {
        this.header = detail
        return this
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param holder the viewHolder of this item
     */
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        //get the context
        val ctx = holder.itemView.context

        //set the background for the item
        holder.view.clearAnimation()
        holder.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
        //set the text for the name
        holder.header.text = header?.intitule

        if (subItems.isEmpty()) {
            holder.icon.visibility = View.GONE
        } else {
            holder.icon.visibility = View.VISIBLE
        }

        if (isExpanded) {
            holder.icon.rotation = 180f
        } else {
            holder.icon.rotation = 0f
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.header.text = null
        //make sure all animations are stopped
        holder.icon.clearAnimation()
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    /**
     * our ViewHolder
     */
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var header: TextView = view.findViewById(R.id.txv_element_child)
        var icon: ImageView = view.findViewById(R.id.material_drawer_icon)
    }
}