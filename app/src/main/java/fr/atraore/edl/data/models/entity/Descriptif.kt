package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Descriptif(
    @PrimaryKey(autoGenerate = false) val id: String,
    val label: String,
    val section: Int,
    val equipement: String
) {
}