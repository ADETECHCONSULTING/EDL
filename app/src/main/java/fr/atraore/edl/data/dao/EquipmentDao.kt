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
    fun getLevelsForItem(itemId: String): LevelData

    @Query("SELECT level1 FROM EquipmentReference WHERE idRoomRef = :idRoomRef OR idRoomRef IS NULL AND level1 IS NOT NULL GROUP BY level1 ORDER BY level1 ASC")
    fun getFirstLevelItems(idRoomRef: Int): Flow<List<String>>

    @Query("SELECT level2 FROM EquipmentReference WHERE idRoomRef = :idRoomRef OR idRoomRef IS NULL AND level2 IS NOT NULL GROUP BY level2 ORDER BY level2 ASC")
    fun getSecondLevelItems(idRoomRef: Int): Flow<List<String>>

    @Query("SELECT level3 FROM EquipmentReference WHERE idRoomRef = :idRoomRef OR idRoomRef IS NULL AND level3 IS NOT NULL GROUP BY level3 ORDER BY level3 ASC")
    fun getThirdLevelItems(idRoomRef: Int): Flow<List<String>>

    @Query("UPDATE EquipmentReference SET level3 = :value, idRoomRef = :idRoomRef WHERE id = :itemId")
    suspend fun updateLevel3(itemId: String, value: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level2 = :value, idRoomRef = :idRoomRef WHERE id = :itemId AND level3 IS NULL")
    suspend fun updateLevel2(itemId: String, value: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level1 = :value, idRoomRef = :idRoomRef WHERE id = :itemId AND level2 IS NULL")
    suspend fun updateLevel1(itemId: String, value: String, idRoomRef: Int)

    @Query("DELETE FROM EquipmentReference WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("INSERT INTO EquipmentReference (level1, level2, level3, idRoomRef) VALUES (:firstItem, null, null, :idRoomRef)")
    suspend fun addItemFirstLevel(firstItem: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level2 = :secondItem WHERE level1 = :level1 AND idRoomRef = :idRoomRef")
    suspend fun updateItemSecondLevel(level1: String, secondItem: String, idRoomRef: Int)

    @Query("UPDATE EquipmentReference SET level3 = :thirdItem WHERE level1 = :level1 AND idRoomRef = :idRoomRef")
    suspend fun updateItemThirdLevel(level1: String, thirdItem: String, idRoomRef: Int)

    @Query("SELECT level1 FROM EquipmentReference WHERE level1 LIKE '%' || :query || '%' GROUP BY level1 ORDER BY level1 ASC")
    fun filterLevelOneItems(query: String): Flow<List<String>>

    @Query("SELECT level2 FROM EquipmentReference WHERE level2 LIKE '%' || :query || '%' GROUP BY level2 ORDER BY level2 ASC")
    fun filterLevelTwoItems(query: String): Flow<List<String>>

    @Query("SELECT level3 FROM EquipmentReference WHERE level3 LIKE '%' || :query || '%' GROUP BY level3 ORDER BY level3 ASC")
    fun filterLevelThreeItems(query: String): Flow<List<String>>
}

data class LevelData(val level2: String?, val level3: String?)
