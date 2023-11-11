package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.EquipmentDao
import fr.atraore.edl.data.models.entity.EquipmentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EquipmentRepository @Inject constructor(
    private val dao: EquipmentDao
): BaseRepository<EquipmentReference>(dao) {
    fun getAllEquipments(): Flow<List<EquipmentReference>> = dao.getAllEquipments()
}