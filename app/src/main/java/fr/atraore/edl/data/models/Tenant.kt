package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Tenant(
    @PrimaryKey(autoGenerate = false) val tenantId: String,
    val civi: String?,
    val name: String,
    val address: String?,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "postal_code") val postalCode: String?,
    val city: String?,
    val tel: String?,
    @ColumnInfo(name = "tel_2") val tel2: String?,
    val mail: String?,
    val notes: String?,
    @ColumnInfo(name = "sortie_address") val sortieAddress: String?,
    @ColumnInfo(name = "sortie_address_2") val sortieAddress2: String?,
    @ColumnInfo(name = "sortie_postal_code") val sortiePostalCode: String?,
    @ColumnInfo(name = "sortie_city") val sortieCity: String?,
    @ColumnInfo(name = "date_entree") val dateEntree: Date
) {
}