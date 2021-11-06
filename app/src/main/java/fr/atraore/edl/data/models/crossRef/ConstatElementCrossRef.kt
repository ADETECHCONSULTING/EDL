package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "elementReferenceId"])
data class ConstatElementCrossRef(
    val constatId: String,
    val elementReferenceId: String
)