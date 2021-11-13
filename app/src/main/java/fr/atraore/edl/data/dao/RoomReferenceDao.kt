package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.data.models.RoomWithElements
import fr.atraore.edl.utils.AGENCY_TABLE
import fr.atraore.edl.utils.ROOM_REFERENCE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomReferenceDao : BaseDao<RoomReference> {
    //** GET **
    //Select all RoomReference
    @Query("SELECT * FROM $ROOM_REFERENCE_TABLE")
    fun getAllRoomReferences(): Flow<List<RoomReference>>

    @Query("SELECT * FROM $ROOM_REFERENCE_TABLE LIMIT 1")
    fun getFirstRoomReferences(): Flow<RoomReference>

    @Transaction
    @Query("SELECT * FROM $ROOM_REFERENCE_TABLE")
    fun getRoomDetails() : Flow<List<RoomWithElements>>
}