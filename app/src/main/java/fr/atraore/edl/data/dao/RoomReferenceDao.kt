package fr.atraore.edl.data.dao

import androidx.room.*
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatUsersCrossRef
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
    @Query("SELECT * FROM $ROOM_REFERENCE_TABLE WHERE idLotReference is null")
    fun getAllRoomReferences(): Flow<List<RoomReference>>

    @Query("SELECT * FROM $ROOM_REFERENCE_TABLE LIMIT 1")
    fun getFirstRoomReferences(): Flow<RoomReference>

    @Transaction
    @Query("SELECT * FROM RoomReference JOIN DETAIL dt ON dt.idRoom = roomReferenceId WHERE dt.idLot = :idLot ORDER BY name asc")
    fun getRoomDetails(idLot: Int) : Flow<Map<RoomReference, List<Detail>>>

    @Transaction
    @Query("SELECT * FROM RoomReference WHERE name = :name AND idLotReference = :idLot")
    fun getRoomWithElements(name: String, idLot: Int) : Flow<RoomWithElements>

    @Transaction
    @Query("SELECT * FROM RoomReference WHERE name = :name AND idLotReference = :idLot")
    fun getRoomWithNameAndIdLot(name: String, idLot: Int) : Flow<RoomReference>

    @Transaction
    @Query("SELECT * FROM RoomReference WHERE idLotReference = :idLot")
    fun getRoomsAndElementsWithIdLot(idLot: Int) : Flow<List<RoomWithElements>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoomElementCrossRef(crossRef: RoomElementCrossRef)

    @Delete
    suspend fun deleteRoomElementCrossRef(crossRef: RoomElementCrossRef)
}