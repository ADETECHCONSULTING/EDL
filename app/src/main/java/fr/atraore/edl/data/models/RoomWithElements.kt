package fr.atraore.edl.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import fr.atraore.edl.data.models.crossRef.RoomElementCrossRef
import java.io.Serializable

data class RoomWithElements (
    @Embedded val rooms: RoomReference,
    @Relation(
        parentColumn = "roomReferenceId",
        entityColumn = "elementReferenceId",
        associateBy = Junction(RoomElementCrossRef::class)
    )
    val elements: MutableList<ElementReference>,
) : Serializable {

}