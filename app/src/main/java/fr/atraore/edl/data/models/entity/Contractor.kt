package fr.atraore.edl.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.CONTRACTOR_TABLE
import java.io.Serializable

@Entity(tableName = CONTRACTOR_TABLE)
data class Contractor (
    @PrimaryKey(autoGenerate = false) val contractorId: String,
    var denomination: String,
    val mail: String?,
    val tel: String?,
    @ColumnInfo(name = "tel_2") val tel2: String?,
    @ColumnInfo(name = "address_1") val address1: String?,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "address_3") val address3: String?,
    @ColumnInfo(name = "postal_code_1") val postalCode1: String?,
    @ColumnInfo(name = "postal_code_2") val postalCode2: String?,
    @ColumnInfo(name = "postal_code_3") val postalCode3: String?,
    val city: String?,
    @ColumnInfo(name = "city_2") val city2: String?,
    val notes: String?
) : PrimaryInfo(), Serializable {
    @Ignore
    override fun primaryInfo(): String {
        return denomination
    }

    override fun civiInfo(): String {
        return ""
    }
}