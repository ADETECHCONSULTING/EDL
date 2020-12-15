package fr.atraore.edl.data.models

//Detail des constats
data class Detail(
    val detailId: String,
    val idConstat: Int,
    val intitule: String?,
    val descriptif: String?,
    val alteration: String?,
    val notes: String?
) {
}