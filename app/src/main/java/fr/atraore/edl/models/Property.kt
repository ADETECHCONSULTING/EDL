package fr.atraore.edl.models

//Biens
data class Property (
    val id: Int,
    val address: String,
    val address2: String?,
    val postalCode: String,
    val city: String,
    val notes: String?,
    val nature: String?, //Studio
    val type: String?, //T3
    val nbLevels: Int?,
    val comment: String?,
    val floor: Int?,
    val stairCase: Int?,
    val appartmentDoor: Int?,
    val caveDoor: Int?,
    val atticDoor: Int?, //grenier
    val parkingDoor: Int?,
    val boxDoor: Int?
) {
}