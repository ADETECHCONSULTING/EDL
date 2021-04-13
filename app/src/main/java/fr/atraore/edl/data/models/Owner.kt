package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.OWNER_TABLE
import java.io.Serializable

@Entity(tableName = OWNER_TABLE)
data class Owner(
    @PrimaryKey(autoGenerate = false) val ownerId: String,
    val civi: String?,
    var name: String,
    val address: String?,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "postal_code") val postalCode: String?,
    val city: String?,
    val tel: String?,
    @ColumnInfo(name = "tel_2") val tel2: String?,
    val mail: String?,
    val notes: String?
) : PrimaryInfo(), Serializable {
    @Ignore
    override fun primaryInfo(): String {
        return name
    }

    override fun civiInfo(): String {
        return "$civi"
    }
}