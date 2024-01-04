package fr.atraore.edl.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.AGENCY_TABLE
import java.io.Serializable

//Agences
@Entity(tableName = AGENCY_TABLE)
data class Agency (
    @PrimaryKey(autoGenerate = false) val agencyId: String,
    val name: String,
    val address: String?,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "address_3") val address3: String?,
    @ColumnInfo(name = "num_tel_fixe") val numTelFixe: String?,
    @ColumnInfo(name = "num_tel_portable") val numTelPortable: String?,
    @ColumnInfo(name = "num_fax") val numFax: String?,
    @ColumnInfo(name = "name_alias") val nameAlias: String?,
    val courante: Int?,
    @ColumnInfo(name = "logo_name") val logoName: String?,
    val actif: Int,
    val alias: String? = null
) : Serializable {

}