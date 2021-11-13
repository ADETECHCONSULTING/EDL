package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.Alteration
import fr.atraore.edl.data.models.Descriptif
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.data.models.Etat

data class ElementWithEtat(
    @Embedded val element: ElementReference,
    @Relation(
        parentColumn = "elementReferenceId",
        entityColumn = "equipement"
    )
    val etats: List<Etat>
) {
}