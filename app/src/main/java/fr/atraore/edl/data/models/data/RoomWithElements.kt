package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import java.io.Serializable

data class RoomWithElements (
    @Embedded val rooms: RoomReference,
    @Relation(
        parentColumn = "roomReferenceId",
        entityColumn = "idRoom"
    )
    val elements: MutableList<ElementReference>,
) : Serializable {

}