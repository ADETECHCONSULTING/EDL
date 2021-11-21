package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Etat(
    @PrimaryKey(autoGenerate = false) val id: String,
    val label: String,
    val section: Int,
    val equipement: String
)