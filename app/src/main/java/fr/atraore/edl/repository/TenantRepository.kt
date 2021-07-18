package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.TenantDao
import fr.atraore.edl.data.models.Tenant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TenantRepository @Inject constructor(
    private val tenantDao: TenantDao
) : BaseRepository<Tenant>(tenantDao) {

    val allTenants: Flow<List<Tenant>> = tenantDao.getAllTenants()
}