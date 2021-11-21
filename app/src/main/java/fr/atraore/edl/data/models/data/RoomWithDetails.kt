package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.data.models.crossRef.RoomDetailCrossRef
import java.io.Serializable

data class RoomWithDetails (
    @Embedded val rooms: RoomReference,
    @Relation(
        parentColumn = "roomReferenceId",
        entityColumn = "idRoom",
    )
    val details: MutableList<Detail>,
) : Serializable {

}