package fr.atraore.edl.data.models

data class Descriptif(
    val id: String,
    val libelle: String,
    val nature: String,
    val bib: String?,
    val parent: String?, //eqp
    val grandParent: String? //pere
) {
}