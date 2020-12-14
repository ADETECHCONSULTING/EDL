package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "ownerId"])
data class ConstatOwnerCrossRef(
    val constatId: Int,
    val ownerId: Int
)