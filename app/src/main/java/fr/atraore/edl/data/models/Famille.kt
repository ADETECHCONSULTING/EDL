package fr.atraore.edl.data.models

data class Famille(
    val id: String,
    val libelle: String, //fam
    val bib: String?,
    val parent: String?, //eqt
    val grandParent: String //parent
) {
}