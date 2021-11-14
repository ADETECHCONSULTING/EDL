package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.ConstatDao
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.crossRef.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConstatRepository @Inject constructor(
    private val constatDao: ConstatDao
) : BaseRepository<Constat>(constatDao) {
    val allConstats: Flow<List<Constat>> = constatDao.getAllConstat()
    val allConstatWithDetails: Flow<List<ConstatWithDetails>> = constatDao.getAllConstatWithDetails()

    fun getConstatDetail(constatId: String): Flow<ConstatWithDetails> {
        return constatDao.getConstatDetails(constatId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatTenantCrossRef(constatId: String, tenantId: String) {
        constatDao.saveConstatTenantCrossRef(ConstatTenantCrossRef(constatId, tenantId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatOwnerCrossRef(constatId: String, ownerId: String) {
        constatDao.saveConstatOwnerCrossRef(ConstatOwnerCrossRef(constatId, ownerId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatContractorCrossRef(constatId: String, contractorId: String) {
        constatDao.saveConstatContractorCrossRef(ConstatContractorCrossRef(constatId, contractorId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatPropertyCrossRef(constatId: String, propertyId: String) {
        constatDao.saveConstatPropertyCrossRef(ConstatPropertyCrossRef(constatId, propertyId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatRoomCrossRef(constatId: String, roomId: String) {
        constatDao.saveConstatRoomCrossRef(ConstatRoomCrossRef(constatId, roomId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatLotCrossRef(constatId: String, lotId: String) {
        constatDao.saveConstatLotCrossRef(ConstatLotCrossRef(constatId, lotId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatAgencyCrossRef(constatId: String, agencyId: String) {
        constatDao.saveConstatAgencyCrossRef(ConstatAgencyCrossRef(constatId, agencyId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveRoomElementCrossRef(roomId: String, elementId: String, rename: String?) {
        constatDao.saveRoomElementCrossRef(RoomElementCrossRef(roomId, elementId, rename))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateExistingAgencyCrossRef(constatId: String, agencyId: String) {
        constatDao.updateExistingAgencyCrossRef(constatId, agencyId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveConstatUserCrossRef(constatId: String, userId: String) {
        constatDao.saveConstatUsersCrossRef(ConstatUsersCrossRef(constatId, userId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateExistingUserCrossRef(constatId: String, userId: String) {
        constatDao.updateExistingUsersCrossRef(constatId, userId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteConstatPropertyCrossRef(constatId: String, propertyId: String) {
        constatDao.deleteConstatPropertyCrossRef(ConstatPropertyCrossRef(constatId, propertyId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteRoomElementCrossRef(roomId: String, elementId: String) {
        constatDao.deleteRoomElementCrossRef(RoomElementCrossRef(roomId, elementId, null))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteConstatOwnerCrossRef(constatId: String, ownerId: String) {
        constatDao.deleteConstatOwnerCrossRef(ConstatOwnerCrossRef(constatId, ownerId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteConstatTenantCrossRef(constatId: String, tenantId: String) {
        constatDao.deleteConstatTenantCrossRef(ConstatTenantCrossRef(constatId, tenantId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteConstatContractorCrossRef(constatId: String, contractorId: String) {
        constatDao.deleteConstatContractorCrossRef(ConstatContractorCrossRef(constatId, contractorId))
    }

}