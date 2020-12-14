package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.CONTRACTOR_TABLE

@Entity(tableName = CONTRACTOR_TABLE)
data class Contractor(
    @PrimaryKey(autoGenerate = true) val contractorId: Int,
    val denomination: String,
    val mail: String?,
    @ColumnInfo(name = "address_1") val address1: String?,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "address_3") val address3: String?,
    @ColumnInfo(name = "postal_code_1") val postalCode1: String?,
    @ColumnInfo(name = "postal_code_2") val postalCode2: String?,
    @ColumnInfo(name = "postal_code_3") val postalCode3: String?,
    val city: String?
) {
}