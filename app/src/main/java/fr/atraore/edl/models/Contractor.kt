package fr.atraore.edl.models

data class Contractor(
    val id: Int,
    val denomination: String,
    val mail: String?,
    val address1: String?,
    val address2: String?,
    val address3: String?,
    val postalCode1: String?,
    val postalCode2: String?,
    val postalCode3: String?,
    val city: String?
) {
}