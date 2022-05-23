package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.OutDoorEquipementDao
import fr.atraore.edl.data.models.entity.OutdoorEquipementReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OutdoorEquipementRepository @Inject constructor(
    private val dao: OutDoorEquipementDao
) : BaseRepository<OutdoorEquipementReference>(dao) {
    fun getAll(): Flow<List<OutdoorEquipementReference>> = dao.getAll()
    fun getAllActifKeysRef(): Flow<List<OutdoorEquipementReference>> = dao.getAllActifKeysRef()
    fun getById(id: String): Flow<OutdoorEquipementReference> = dao.getById(id)
    fun searchOutdoorQuery(query: String): Flow<List<OutdoorEquipementReference>> = dao.searchKeysQuery(query)
}