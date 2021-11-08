package fr.atraore.edl.data.dao

import androidx.room.*
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.utils.CONSTAT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstatDao : BaseDao<Constat> {
    //** GET **
    //Select all constat from Table Constat
    @Query("SELECT * FROM $CONSTAT_TABLE")
    fun getAllConstat(): Flow<List<Constat>>

    @Transaction
    @Query("SELECT * FROM $CONSTAT_TABLE")
    fun getAllConstatWithDetails() : Flow<List<ConstatWithDetails>>

    @Transaction
    @Query("SELECT * FROM constat WHERE constatId = :constatId")
    fun getConstatDetails(constatId: String) : Flow<ConstatWithDetails>

    //Select constat by id
    @Query("SELECT * FROM $CONSTAT_TABLE WHERE constatId = :constatId")
    fun getConstatById(constatId: Int): Flow<Constat>

    //** INSERT **
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatContractorCrossRef(crossRef: ConstatContractorCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatOwnerCrossRef(crossRef: ConstatOwnerCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatTenantCrossRef(crossRef: ConstatTenantCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatPropertyCrossRef(crossRef: ConstatPropertyCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatAgencyCrossRef(crossRef: ConstatAgencyCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatUsersCrossRef(crossRef: ConstatUsersCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatRoomCrossRef(crossRef: ConstatRoomCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatElementCrossRef(crossRef: ConstatElementCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatLotCrossRef(crossRef: ConstatLotCrossRef)

    //** Update **
    @Query("UPDATE ConstatAgencyCrossRef SET agencyId =:agencyId WHERE constatId =:constatId")
    suspend fun updateExistingAgencyCrossRef(constatId: String, agencyId: String)

    @Query("UPDATE ConstatUsersCrossRef SET userId =:userId WHERE constatId =:constatId")
    suspend fun updateExistingUsersCrossRef(constatId: String, userId: String)

    //** DELETE **
    @Delete
    fun delete(constat: Constat)

    @Delete
    suspend fun deleteConstatPropertyCrossRef(crossRef: ConstatPropertyCrossRef)

    @Delete
    suspend fun deleteConstatOwnerCrossRef(crossRef: ConstatOwnerCrossRef)

    @Delete
    suspend fun deleteConstatTenantCrossRef(crossRef: ConstatTenantCrossRef)

    @Delete
    suspend fun deleteConstatContractorCrossRef(crossRef: ConstatContractorCrossRef)

    @Delete
    suspend fun deleteConstatAgencyCrossRef(crossRef: ConstatAgencyCrossRef)

    @Delete
    suspend fun deleteConstatUsersCrossRef(crossRef: ConstatUsersCrossRef)

    @Query("DELETE FROM $CONSTAT_TABLE")
    fun deleteAll()
}