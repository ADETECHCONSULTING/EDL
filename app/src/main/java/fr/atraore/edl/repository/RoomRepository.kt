package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.RoomReferenceDao
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef
import fr.atraore.edl.data.models.crossRef.RoomElementCrossRef
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomReferenceDao: RoomReferenceDao
) : BaseRepository<RoomReference>(roomReferenceDao) {

    fun allRoomReferences(): Flow<List<RoomReference>> = roomReferenceDao.getAllRoomReferences()

    fun firstRoomReference() : Flow<RoomReference> = roomReferenceDao.getFirstRoomReferences()

    fun getRoomDetails(idLot: Int) : Flow<Map<RoomReference, List<Detail>>> = roomReferenceDao.getRoomDetails(idLot)

    fun getRoomWithElements(idRoom: String) : Flow<RoomWithElements> = roomReferenceDao.getRoomWithElements(idRoom)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveRoomElementCrossRef(idRoom: String, idElement: String) {
        roomReferenceDao.saveRoomElementCrossRef(RoomElementCrossRef(idRoom, idElement))
    }
}