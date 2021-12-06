package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.utils.OWNER_TABLE
import fr.atraore.edl.utils.TENANT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao: BaseDao<Owner> {
    //** GET **
    //Select all propriétaire
    @Query("SELECT * FROM $OWNER_TABLE")
    fun getAllOwners(): Flow<List<Owner>>

    @Query("SELECT * FROM $OWNER_TABLE WHERE ownerId = :id")
    fun getById(id: String): Flow<Owner>

    @Update
    suspend fun saveOwners(owners: List<Owner>)
}