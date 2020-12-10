package fr.atraore.edl.data.models

const val STATES_ID_CONFIG = 1

data class ConfigStates(
    val textStateNew: String, //Neuf
    val textStateGood: String, //Bon
    val textStateUsed: String, //D'usage
    val textStateBad: String, //Mauvais
    val textStateHS: String, //H.S.
) {
    var id: Int = STATES_ID_CONFIG
}