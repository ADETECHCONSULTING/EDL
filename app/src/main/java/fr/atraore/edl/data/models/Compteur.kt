package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import fr.atraore.edl.utils.COMPTEUR_TABLE

@Entity(tableName = COMPTEUR_TABLE, primaryKeys = ["constat_id", "compteur_ref_id"])
data class Compteur(
    @ColumnInfo(name = "constat_id") val constatId: String,
    @ColumnInfo(name = "compteur_ref_id") val compteurRefId: String,
    val etat: Boolean?,
    @ColumnInfo(name = "primary_quantity") val primaryQuantity: String?,
    @ColumnInfo(name = "secondary_quantity") val secondaryQuantity: String?,
    val localisation: String?,
    @ColumnInfo(name = "motif_non_releve") val motifNonReleve: String?,
) {
}