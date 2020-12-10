package fr.atraore.edl.data.models

const val CLEANESS_CONFIG_ID = 1
data class ConfigClean(
    val textCleanessClean: String,
    val textCleanessDust: String, //Poussière
    val textCleanessFat: String, //Gras
    val textCleanessUnClean: String, //Non nettoyé
) {
    var id: Int = CLEANESS_CONFIG_ID
}