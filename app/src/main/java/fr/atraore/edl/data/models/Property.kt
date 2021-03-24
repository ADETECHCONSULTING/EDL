package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//Biens
@Entity
data class Property (
    @PrimaryKey(autoGenerate = false) val propertyId: String,
    val address: String,
    @ColumnInfo(name = "address_2") val address2: String?,
    @ColumnInfo(name = "postal_code") val postalCode: String,
    val city: String,
    val notes: String?,
    val nature: String?, //Studio
    val type: String?, //T3
    @ColumnInfo(name = "nb_level") val nbLevels: Int?,
    val comment: String?,
    val floor: Int?,
    @ColumnInfo(name = "stair_case") val stairCase: Int?,
    @ColumnInfo(name = "apparment_door") val appartmentDoor: Int?,
    @ColumnInfo(name = "cave_door") val caveDoor: Int?,
    @ColumnInfo(name = "attic_door") val atticDoor: Int?, //grenier
    @ColumnInfo(name = "parking_door") val parkingDoor: Int?,
    @ColumnInfo(name = "box_door") val boxDoor: Int?
) : PrimaryInfo() {
    @Ignore
    override fun primaryInfo(): String {
        return "$type $nature\n$address $city $postalCode"
    }
}