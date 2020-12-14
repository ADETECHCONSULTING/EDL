package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "propertyId"])
data class ConstatPropertyCrossRef(
    val constatId: Int,
    val propertyId: Int
)