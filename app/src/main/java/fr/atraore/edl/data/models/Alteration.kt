package fr.atraore.edl.data.models

data class Alteration(
    val alterationId: Int,
    val libelle: String,
    val nature: String?,
    val bib: String?,
    val parent: String?, //eqp
    val grandParent: String? //pere
) {
}