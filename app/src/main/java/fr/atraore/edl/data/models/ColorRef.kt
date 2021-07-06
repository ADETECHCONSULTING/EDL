package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ColorRef(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val RGB: String
)