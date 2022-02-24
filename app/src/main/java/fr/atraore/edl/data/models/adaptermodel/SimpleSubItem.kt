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
import com.mikepenz.fastadapter.IExpandable
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.Detail

class SimpleSubItem(val actionHandler: IActionHandler) : AbstractExpandableItem<SimpleSubItem.ViewHolder>(), IExpandable<SimpleSubItem.ViewHolder>, IClickable<SimpleSubItem> {
    private val TAG = SimpleSubItem::class.simpleName

    interface IActionHandler {
        fun onSimpleClick(detail: Detail)
        fun onLongClick(anchorView: View, detail: Detail)
    }

    var header: Detail? = null
    private var mOnClickListener: ClickListener<SimpleSubItem>? = null
    private var selectedPos = RecyclerView.NO_POSITION

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

    //we define a clickListener in here so we can directly animate
    /**
     * we overwrite the item specific click listener so we can automatically animate within the item
     *
     * @return
     */
    @Suppress("SetterBackingFieldAssignment")
    override var onItemClickListener: ClickListener<SimpleSubItem>? = { v: View?, adapter: IAdapter<SimpleSubItem>, item: SimpleSubItem, position: Int ->
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

    override var onPreItemClickListener: ClickListener<SimpleSubItem>?
        get() = null
        set(_) {}


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
        holder.itemView.isSelected = selectedPos == holder.absoluteAdapterPosition

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

        holder.itemView.setOnClickListener {
            header?.let {
                selectedPos = holder.absoluteAdapterPosition
                actionHandler.onSimpleClick(it)
                Log.d(TAG, "bindView: position selected ${selectedPos} vs holder pos ${holder.absoluteAdapterPosition}")
            }
        }

        holder.itemView.setOnLongClickListener {
            header?.let {
                actionHandler.onLongClick(holder.itemView, it)
            }
            true
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