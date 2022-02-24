package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference

data class ElementWithDetail (
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "idElement"
    )
    val details: Detail
) {
}