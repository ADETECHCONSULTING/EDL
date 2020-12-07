package fr.atraore.edl.models

data class Owner(
    val id: Int,
    val civi: String?,
    val name: String,
    val address: String?,
    val address2: String?,
    val postalCode: String?,
    val city: String?,
    val tel: String?,
    val tel2: String?,
    val mail: String?,
    val notes: String?
) {

}