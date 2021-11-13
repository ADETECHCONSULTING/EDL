package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.ROOM_REFERENCE_TABLE

//Pieces
@Entity(tableName = ROOM_REFERENCE_TABLE)
data class RoomReference (
    @PrimaryKey(autoGenerate = false) val roomReferenceId: String,
    val name: String,
    var mandatory: Boolean? = false,
) {
}