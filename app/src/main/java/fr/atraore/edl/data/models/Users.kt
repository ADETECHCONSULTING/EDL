package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.USERS_TABLE

@Entity(tableName = USERS_TABLE)
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val civi: String,
    val name: String,
    val address: String?,
    @ColumnInfo(name = "address_2") val address2: String,
    @ColumnInfo(name = "postal_code") val postalCode: String?,
    val city: String,
    val tel: String?,
    @ColumnInfo(name = "tel_2") val tel2: String?,
    val mail: String?,
    val notes: String?,
    val actif: Int
) {
}