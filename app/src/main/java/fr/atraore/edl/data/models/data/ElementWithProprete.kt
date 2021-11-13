package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.*

data class ElementWithProprete(
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val propretes: List<Proprete>
) {
}