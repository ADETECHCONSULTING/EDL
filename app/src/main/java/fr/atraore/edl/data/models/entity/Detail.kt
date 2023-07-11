package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//Detail des constats
@Entity
data class Detail (
    @PrimaryKey(autoGenerate = false) val idDetail: String,
    val idElement: String?,
    val idRoom: String?,
    val idConstat: String,
    val idLot: Int,
    var intitule: String,
    val idKey: Int? = null,
    val idOutDoorEqpt: Int? = null,
    var etat: String? = "",
    var descriptif: String? = "",
    var alteration: String? = "",
    var proprete: String? = "",
    var notes: String? = "",
    var fonctionmt: String? = null,
    var imagesPaths: String? = null,
    var nature: String? = null
) {
    @Ignore
    fun razDetail() {
        this.etat = ""
        this.descriptif = ""
        this.alteration = ""
        this.proprete = ""
        this.notes = ""
        this.nature = ""
    }
}