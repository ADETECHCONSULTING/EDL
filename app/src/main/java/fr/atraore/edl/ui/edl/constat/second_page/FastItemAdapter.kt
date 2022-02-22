package fr.atraore.edl.ui.edl.constat.second_page

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
import com.mikepenz.fastadapter.adapters.ItemFilter

/**
 * Kotlin type alias to simplify usage for an all accepting FastItemAdapter
 */
typealias GenericFastItemAdapter = FastItemAdapter<GenericItem>

class FastItemAdapter<Item: GenericItem>(
    val itemAdapter: ItemAdapter<Item> = items()
) : IItemAdapter<Item, Item> by itemAdapter, FastAdapter<Item>() {

    val itemFilter: ItemFilter<*, Item>
        get() = itemAdapter.itemFilter

    init {
        addAdapter<IAdapter<Item>>(0, itemAdapter)
        cacheSizes()
    }

    fun withUseIdDistributor(useIdDistributor: Boolean): FastItemAdapter<Item> {
        itemAdapter.isUseIdDistributor = useIdDistributor
        return this
    }

    fun removeItemRange(position: Int, itemCount: Int): FastItemAdapter<Item> {
        removeRange(position, itemCount)
        return this
    }

    fun remapMappedTypes() {
        itemAdapter.remapMappedTypes()
    }


}