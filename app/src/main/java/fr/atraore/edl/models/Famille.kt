package fr.atraore.edl.models

data class Famille(
    val id: Int,
    val libelle: String, //fam
    val bib: String?,
    val parent: String?, //eqt
    val grandParent: String //parent
) {
}