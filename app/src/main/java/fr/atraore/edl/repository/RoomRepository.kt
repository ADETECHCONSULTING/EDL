package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.RoomReferenceDao
import fr.atraore.edl.data.models.crossRef.RoomConstatCrossRef
import fr.atraore.edl.data.models.crossRef.RoomElementCrossRef
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomReferenceDao: RoomReferenceDao
) : BaseRepository<RoomReference>(roomReferenceDao) {

    fun getAllRoomReferences(): Flow<List<RoomReference>> = roomReferenceDao.getAllRoomReferences()

    fun getRoomWithElements(name: String, idLot: Int) : Flow<RoomWithElements> = roomReferenceDao.getRoomWithElements(name, idLot)
    fun getRoomWithNameAndIdLot(name: String, idLot: Int) : Flow<RoomReference> = roomReferenceDao.getRoomWithNameAndIdLot(name, idLot)
    fun getRoomsForConstat(constatId: String) : Flow<List<RoomReference>> = roomReferenceDao.getRoomsForConstat(constatId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveRoomConstatCrossRef(crossRefs: List<RoomConstatCrossRef>) = roomReferenceDao.saveRoomConstatCrossRef(crossRefs)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveRoomElementCrossRef(idRoom: String, idElement: String) {
        roomReferenceDao.saveRoomElementCrossRef(RoomElementCrossRef(idRoom, idElement))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteRoomElementCrossRef(idRoom: String, idElement: String) {
        roomReferenceDao.deleteRoomElementCrossRef(RoomElementCrossRef(idRoom, idElement))
    }
}