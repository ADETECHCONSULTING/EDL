package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EquipmentReference(
    @PrimaryKey(autoGenerate = false) val id: String,
    val level1: String,
    val level2: String,
    val level3: String?
)
