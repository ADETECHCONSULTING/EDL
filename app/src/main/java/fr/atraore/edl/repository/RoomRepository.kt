package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.AgencyDao
import fr.atraore.edl.data.dao.ElementReferenceDao
import fr.atraore.edl.data.dao.RoomReferenceDao
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.data.models.RoomWithElements
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomReferenceDao: RoomReferenceDao
) : BaseRepository<RoomReference>(roomReferenceDao) {

    val allRoomReferences: Flow<List<RoomReference>> = roomReferenceDao.getAllRoomReferences()

    fun firstRoomReference() : Flow<RoomReference> = roomReferenceDao.getFirstRoomReferences()

    fun getRoomDetails(roomReferenceId: String) : Flow<RoomWithElements> = roomReferenceDao.getRoomDetails(roomReferenceId)
}