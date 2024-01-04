package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.OutdoorEquipementReference
import fr.atraore.edl.utils.OUTDOOR_EQUIPMNT
import kotlinx.coroutines.flow.Flow

@Dao
interface OutDoorEquipementDao : BaseDao<OutdoorEquipementReference> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM $OUTDOOR_EQUIPMNT")
    fun getAll(): Flow<List<OutdoorEquipementReference>>

    @Query("SELECT * FROM $OUTDOOR_EQUIPMNT WHERE actif = 1")
    fun getAllActifRef(): Flow<List<OutdoorEquipementReference>>

    @Query("SELECT * FROM $OUTDOOR_EQUIPMNT WHERE id =:id")
    fun getById(id: String): Flow<OutdoorEquipementReference>

    @Query("SELECT * FROM $OUTDOOR_EQUIPMNT WHERE name LIKE :searchQuery")
    fun searchKeysQuery(searchQuery: String) : Flow<List<OutdoorEquipementReference>>
}