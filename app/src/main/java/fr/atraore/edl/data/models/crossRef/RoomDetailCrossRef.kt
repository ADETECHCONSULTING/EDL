package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["roomReferenceId", "idDetail"])
data class RoomDetailCrossRef(
    val roomReferenceId: String,
    val idDetail: String
    )