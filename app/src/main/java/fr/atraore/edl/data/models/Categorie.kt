package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Categorie(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val section: String,
    val equipement: String
)