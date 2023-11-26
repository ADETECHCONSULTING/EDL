package fr.atraore.edl.utils

interface OnTreeNodeClickListener {
    fun onNodeClicked(itemId: String?, name: String, idRoomRef: Int? = null)
    fun onNodeLongClicked(itemId: String?, name: String, idRoomRef: Int? = null)
}