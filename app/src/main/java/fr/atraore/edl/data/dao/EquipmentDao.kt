package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.EquipmentReference
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao : BaseDao<EquipmentReference> {
    @Query("SELECT DISTINCT * FROM EquipmentReference")
    fun getAllEquipments(): Flow<List<EquipmentReference>>
}
