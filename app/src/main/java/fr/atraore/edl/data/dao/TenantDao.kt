package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.utils.TENANT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao : BaseDao<Tenant> {
    //** GET **
    //Select all locataires
    @Query("SELECT * FROM $TENANT_TABLE")
    fun getAllTenants(): Flow<List<Tenant>>

    @Query("SELECT * FROM $TENANT_TABLE WHERE tenantId = :id")
    fun getById(id: String): Flow<Tenant>
}