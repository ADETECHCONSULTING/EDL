package fr.atraore.edl.data.models.adaptermodel

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IClickable
import com.mikepenz.fastadapter.ISubItem
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.Detail

class SimpleParentExpandableItem : AbstractExpandableItem<SimpleParentExpandableItem.ViewHolder>(), IClickable<SimpleParentExpandableItem>, ISubItem<SimpleParentExpandableItem.ViewHolder> {

    var header: String? = null
    var detail: Detail? = null
    private var selectedPos = RecyclerView.NO_POSITION
    lateinit var actionHandler: IActionHandler

    private var mOnClickListener: ClickListener<SimpleParentExpandableItem>? = null

    interface IActionHandler {
        fun onSimpleKeyClick(detail: Detail)
    }

    //we define a clickListener in here so we can directly animate
    /**
     * we overwrite the item specific click listener so we can automatically animate within the item
     *
     * @return
     */
    @Suppress("SetterBackingFieldAssignment")
    override var onItemClickListener: ClickListener<SimpleParentExpandableItem>? = { v: View?, adapter: IAdapter<SimpleParentExpandableItem>, item: SimpleParentExpandableItem, position: Int ->
        if (item.subItems.isNotEmpty()) {
            v?.findViewById<View>(R.id.material_drawer_icon)?.let {
                if (!item.isExpanded) {
                    ViewCompat.animate(it).rotation(180f).start()
                } else {
                    ViewCompat.animate(it).rotation(0f).start()
                }
            }
        }
        mOnClickListener?.invoke(v, adapter, item, position) ?: true
    }
        set(onClickListener) {
            this.mOnClickListener = onClickListener // on purpose
        }

    override var onPreItemClickListener: ClickListener<SimpleParentExpandableItem>?
        get() = null
        set(_) {}

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_expandable_item_id

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    override val layoutRes: Int
        get() = R.layout.room_list_item

    fun withHeader(header: String): SimpleParentExpandableItem {
        this.header = header
        return this
    }

    fun withDetail(detail: Detail): SimpleParentExpandableItem {
        this.detail = detail
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
        holder.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorAccent))
        //set the text for the name
        holder.header.text = header

        if (subItems.isEmpty()) {
            holder.icon.visibility = View.GONE
        } else {
            holder.icon.visibility = View.VISIBLE
        }

        if (isExpanded) {
            holder.icon.rotation = 0f
        } else {
            holder.icon.rotation = 180f
        }

        detail?.let { detail ->
            holder.itemView.setOnClickListener {
                detail.let {
                    selectedPos = holder.absoluteAdapterPosition
                    actionHandler.onSimpleKeyClick(it)
                }
            }
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
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var header: TextView = view.findViewById(R.id.txv_room_parent)
        var icon: ImageView = view.findViewById(R.id.material_drawer_icon)
    }
}