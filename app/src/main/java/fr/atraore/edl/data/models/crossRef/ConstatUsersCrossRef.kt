package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "userId"])
data class ConstatUsersCrossRef(
    val constatId: String,
    val userId: String
)