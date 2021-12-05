package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.data.models.data.RoomWithDetails
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
    @Query("SELECT * FROM RoomReference JOIN DETAIL dt ON dt.idRoom = roomReferenceId WHERE dt.idLot = :idLot ORDER BY name asc")
    fun getRoomDetails(idLot: Int) : Flow<Map<RoomReference, List<Detail>>>
}