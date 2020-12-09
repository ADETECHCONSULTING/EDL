package fr.atraore.edl.models

data class Alteration(
    val id: Int,
    val libelle: String,
    val nature: String?,
    val bib: String?,
    val parent: String?, //eqp
    val grandParent: String? //pere
) {
}