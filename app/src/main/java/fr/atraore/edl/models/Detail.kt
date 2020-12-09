package fr.atraore.edl.models

//Detail des constats
data class Detail(
    val id: Int,
    val idConstat: Int,
    val intitule: String?,
    val descriptif: String?,
    val alteration: String?,
    val notes: String?
) {
}