package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.ROOM_REFERENCE_TABLE
import java.io.Serializable

//Pieces
@Entity(tableName = ROOM_REFERENCE_TABLE)
data class RoomReference (
    @PrimaryKey(autoGenerate = false) val roomReferenceId: String,
    val name: String,
    val idLotReference: Int? = null,
    var mandatory: Boolean? = false
) : Serializable {
}