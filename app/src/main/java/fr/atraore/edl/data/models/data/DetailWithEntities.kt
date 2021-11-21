package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.*

data class DetailWithEntities(
    @Embedded val detail: Detail,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "idElement"
    )
    val elementReference: ElementReference,
    @Relation(
        parentColumn = "id",
        entityColumn = "idEtat"
    )
    val etat: Etat,
    @Relation(
        parentColumn = "id",
        entityColumn = "idDescriptif"
    )
    val descriptif: Descriptif,
    @Relation(
        parentColumn = "id",
        entityColumn = "idAlteration"
    )
    val alteration: Alteration
) {
}