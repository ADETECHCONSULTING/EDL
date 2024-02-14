package fr.atraore.edl.repository

import androidx.lifecycle.LiveData
import fr.atraore.edl.data.dao.EquipmentDao
import fr.atraore.edl.data.models.entity.EquipmentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EquipmentRepository @Inject constructor(
    private val dao: EquipmentDao
): BaseRepository<EquipmentReference>(dao) {
    fun getAllEquipments(clickedLot: Int): Flow<List<EquipmentReference>> = dao.getAllEquipments(clickedLot)

    fun getFirstLevelItems(idRoomRef: Int): Flow<List<String>> = dao.getFirstLevelItems(idRoomRef)
    fun getSecondLevelItems(idRoomRef: Int): Flow<List<String>> = dao.getSecondLevelItems(idRoomRef)
    fun getThirdLevelItems(idRoomRef: Int): Flow<List<String>> = dao.getThirdLevelItems(idRoomRef)

    fun filterLevelOneItems(query: String): Flow<List<String>> = dao.filterLevelOneItems(query)
    fun filterLevelTwoItems(query: String): Flow<List<String>> = dao.filterLevelTwoItems(query)
    fun filterLevelThreeItems(query: String): Flow<List<String>> = dao.filterLevelThreeItems(query)

    fun getAllEquipmentReferences(): Flow<List<EquipmentReference>> = dao.getAllEquipmentReferencesWithIdLot()
    suspend fun deleteEquipmentRef(id: String) = dao.deleteById(id)
    suspend fun updateEquipmentReference(itemId: String, value: String, idRoomRef: Int) {
        val levels = dao.getLevelsForItem(itemId)

        when {
            levels.level3 != null -> dao.updateLevel3(itemId, value, idRoomRef)
            levels.level2 != null -> dao.updateLevel2(itemId, value, idRoomRef)
            else -> dao.updateLevel1(itemId, value, idRoomRef)
        }
    }

    fun equipmentExists(levelOne: String, levelTwo: String, levelThree: String, lotId: Int, idRoomRef: Int): Flow<Boolean> {
        return dao.equipmentExists(levelOne, levelTwo, levelThree, lotId, idRoomRef)
    }

}