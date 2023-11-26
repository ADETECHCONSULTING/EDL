package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.EquipmentReference
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao : BaseDao<EquipmentReference> {
    @Query("SELECT DISTINCT * FROM EquipmentReference")
    fun getAllEquipments(): Flow<List<EquipmentReference>>

    @Query("SELECT level2, level3 FROM EquipmentReference WHERE id = :itemId")
    suspend fun getLevelsForItem(itemId: String): LevelData

    @Query("UPDATE EquipmentReference SET level3 = :value, idRoomRef = :idRoomRef WHERE id = :itemId")
    suspend fun updateLevel3(itemId: String, value: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level2 = :value, idRoomRef = :idRoomRef WHERE id = :itemId AND level3 IS NULL")
    suspend fun updateLevel2(itemId: String, value: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level1 = :value, idRoomRef = :idRoomRef WHERE id = :itemId AND level2 IS NULL")
    suspend fun updateLevel1(itemId: String, value: String, idRoomRef: Int)

    @Query("DELETE FROM EquipmentReference WHERE id = :id")
    suspend fun deleteById(id: String)
}

data class LevelData(val level2: String?, val level3: String?)
