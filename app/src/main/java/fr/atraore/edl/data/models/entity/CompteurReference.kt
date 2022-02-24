package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.COMPTEUR_REFERENCE_TABLE

@Entity(tableName = COMPTEUR_REFERENCE_TABLE)
data class CompteurReference (
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    var mandatory: Boolean? = false
)