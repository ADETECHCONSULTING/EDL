package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.RoomReferenceDao
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
}