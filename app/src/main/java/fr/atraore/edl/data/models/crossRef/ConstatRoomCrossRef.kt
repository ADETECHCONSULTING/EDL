package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "roomReferenceId"])
data class ConstatRoomCrossRef (
    val constatId: String,
    val roomReferenceId: String
)