package fr.atraore.edl.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.USERS_TABLE
import java.io.Serializable

@Entity(tableName = USERS_TABLE)
data class Users(
    @PrimaryKey(autoGenerate = false) val userId: String,
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
) : Serializable {
}