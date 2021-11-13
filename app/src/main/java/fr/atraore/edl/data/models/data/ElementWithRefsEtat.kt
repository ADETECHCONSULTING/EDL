package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.*

data class ElementWithRefsEtat(
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val alterations: List<Alteration>,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val descriptifs: List<Descriptif>,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val etats: List<Etat>,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val propretes: List<Proprete>
) {
}