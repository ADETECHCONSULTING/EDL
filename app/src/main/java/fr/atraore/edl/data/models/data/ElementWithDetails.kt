package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.ElementReference

data class ElementWithDetail (
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "idElement"
    )
    val details: Detail
) {
}