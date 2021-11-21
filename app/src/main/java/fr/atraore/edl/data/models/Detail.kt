package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//Detail des constats
@Entity
data class Detail (
    @PrimaryKey(autoGenerate = false) val idDetail: String,
    val idElement: String,
    val idRoom: String,
    val intitule: String,
    val idEtat: String? = "",
    val idDescriptif: String? = "",
    val idAlteration: String? = "",
    val notes: String? = ""
) {
}