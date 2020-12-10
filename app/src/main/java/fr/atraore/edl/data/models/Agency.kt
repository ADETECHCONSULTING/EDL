package fr.atraore.edl.data.models

//Agences
data class Agency (
    val id: Int,
    val name: String,
    val address: String?,
    val address2: String?,
    val address3: String?,
    val numTelFixe: String?,
    val numTelPortable: String?,
    val numFax: String?,
    val nameAlias: String?,
    val courante: Int?,
    val logoName: String?
) {

}