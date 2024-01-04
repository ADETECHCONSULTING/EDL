package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.EquipmentDao
import fr.atraore.edl.data.models.entity.EquipmentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EquipmentRepository @Inject constructor(
    private val dao: EquipmentDao
): BaseRepository<EquipmentReference>(dao) {
    fun getAllEquipments(): Flow<List<EquipmentReference>> = dao.getAllEquipments()

    suspend fun deleteEquipmentRef(id: String) = dao.deleteById(id)
    suspend fun updateEquipmentReference(itemId: String, value: String, idRoomRef: Int) {
        val levels = dao.getLevelsForItem(itemId)

        when {
            levels.level3 != null -> dao.updateLevel3(itemId, value, idRoomRef)
            levels.level2 != null -> dao.updateLevel2(itemId, value, idRoomRef)
            else -> dao.updateLevel1(itemId, value, idRoomRef)
        }
    }

}