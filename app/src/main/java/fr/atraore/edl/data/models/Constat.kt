package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

const val CONSTAT_TABLE = "Constat"

@Entity(tableName = CONSTAT_TABLE)
data class Constat(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type_constat") val typeConstat: String, //ESP - Entrant - Pre-etat - Sortant
    @ColumnInfo(name = "date_creation") val dateCreation: Date,
    @ColumnInfo(name = "id_property") val idProperty: Int?,
    @ColumnInfo(name = "id_tenant") val idTenant: Int?,
    @ColumnInfo(name = "id_owner") val idOwner: Int?,
    @ColumnInfo(name = "id_user") val idUser: Int?,
    @ColumnInfo(name = "id_agency") val idAgency: Int?,
    @ColumnInfo(name = "id_contractor") val idContractor: Int?,
    val state: Int?
)