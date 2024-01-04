package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["roomReferenceId", "constatId"])
data class RoomConstatCrossRef(
    val roomReferenceId: String,
    val constatId: String
)