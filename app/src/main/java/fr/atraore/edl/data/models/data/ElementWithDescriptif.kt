package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.Alteration
import fr.atraore.edl.data.models.Descriptif
import fr.atraore.edl.data.models.ElementReference

data class ElementWithDescriptif(
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val descriptifs: List<Descriptif>
) {
}