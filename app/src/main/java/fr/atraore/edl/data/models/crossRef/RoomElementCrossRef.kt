package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["roomReferenceId", "elementReferenceId"])
data class RoomElementCrossRef(
    val roomReferenceId: String,
    val elementReferenceId: String,
    val rename: String?
)