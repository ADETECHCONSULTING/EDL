package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EquipmentReference(
    @PrimaryKey(autoGenerate = false) val id: String,
    var level1: String,
    var level2: String,
    var level3: String?,
    val idRoomRef: Int?,
    val idLot: Int?
)
