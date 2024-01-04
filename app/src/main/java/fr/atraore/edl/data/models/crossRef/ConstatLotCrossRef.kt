package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "lotReferenceId"])
data class ConstatLotCrossRef(
    val constatId: String,
    val lotReferenceId: Int
)