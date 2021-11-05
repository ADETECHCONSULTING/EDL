package fr.atraore.edl.data.models.crossRef

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "ownerId"])
data class ConstatOwnerCrossRef(
    val constatId: String,
    @ColumnInfo(index = true) val ownerId: String
)