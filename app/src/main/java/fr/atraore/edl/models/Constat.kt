package fr.atraore.edl.models

import org.threeten.bp.format.DateTimeFormatter
import java.sql.Date

data class Constat(
    val id: Int,
    val typeConstat: String, //ESP - Entrant - Pre-etat - Sortant
    val dateCreation: Date,
    val idProperty: Int?,
    val idTenant: Int?,
    val idOwner: Int?,
    val idUser: Int?,
    val idAgency: Int?,
    val idContractor: Int?,
    val state: Int?
) {

}