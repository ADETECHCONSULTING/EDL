package fr.atraore.edl.data.dao

import androidx.room.*
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef
import fr.atraore.edl.data.models.crossRef.RoomElementCrossRef
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.RoomReference
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

    @Transaction
    @Query("SELECT * FROM RoomReference WHERE roomReferenceId = :idRoom")
    fun getRoomWithElements(idRoom: String) : Flow<RoomWithElements>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoomElementCrossRef(crossRef: RoomElementCrossRef)
}