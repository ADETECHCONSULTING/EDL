package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.ConstatDao
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.crossRef.ConstatContractorCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatOwnerCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatPropertyCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef
import kotlinx.coroutines.flow.Flow

class ConstatRepository(
    private val constatDao: ConstatDao
) {
    val allConstats: Flow<List<Constat>> = constatDao.getAllConstat()
    val allConstatWithDetails: Flow<List<ConstatWithDetails>> = constatDao.getAllConstatWithDetails()

    fun getConstatDetail(constatId: String): Flow<ConstatWithDetails> {
        return constatDao.getConstatDetails(constatId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun save(constat: Constat) {
        constatDao.save(constat)
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
}