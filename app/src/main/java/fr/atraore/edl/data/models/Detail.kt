package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//Detail des constats
@Entity
data class Detail (
    @PrimaryKey(autoGenerate = false) val idDetail: String,
    val idElement: String,
    val idRoom: String,
    val idConstat: String,
    val idLot: Int,
    var intitule: String,
    var etat: String? = "",
    var descriptif: String? = "",
    var alteration: String? = "",
    var proprete: String? = "",
    var notes: String? = ""
) {
    @Ignore
    fun razDetail() {
        this.etat = ""
        this.descriptif = ""
        this.alteration = ""
        this.proprete = ""
        this.notes = ""
    }
}