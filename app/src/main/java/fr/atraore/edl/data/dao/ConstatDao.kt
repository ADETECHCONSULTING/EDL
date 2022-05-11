package fr.atraore.edl.data.dao

import androidx.room.*
import fr.atraore.edl.data.models.entity.Constat
import fr.atraore.edl.data.models.data.ConstatWithDetails
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
    fun getAllConstatWithDetails(): Flow<List<ConstatWithDetails>>

    @Transaction
    @Query("SELECT * FROM constat WHERE constatId = :constatId")
    fun getConstatDetails(constatId: String): Flow<ConstatWithDetails>

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
    suspend fun saveConstatElementCrossRef(crossRef: RoomDetailCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConstatLotCrossRef(crossRef: ConstatLotCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoomDetailCrossRef(crossRef: RoomDetailCrossRef)

    //** Update **
    @Query("UPDATE ConstatAgencyCrossRef SET agencyId =:agencyId WHERE constatId =:constatId")
    suspend fun updateExistingAgencyCrossRef(constatId: String, agencyId: String)

    @Query("UPDATE ConstatUsersCrossRef SET userId =:userId WHERE constatId =:constatId")
    suspend fun updateExistingUsersCrossRef(constatId: String, userId: String)

    @Query("UPDATE Constat SET procuration =:procuration WHERE constatId =:constatId")
    suspend fun updateProcuration(constatId: String, procuration: String)

    //** DELETE **
    @Delete
    fun delete(constat: Constat)

    @Delete
    suspend fun deleteConstatPropertyCrossRef(crossRef: ConstatPropertyCrossRef)

    @Delete
    suspend fun deleteConstatRoomCrossRef(crossRef: ConstatRoomCrossRef)

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

    @Delete
    suspend fun deleteRoomDetailCrossRef(crossRef: RoomDetailCrossRef)

    @Query("DELETE FROM $CONSTAT_TABLE")
    fun deleteAll()

    @Query("DELETE FROM CONSTATPROPERTYCROSSREF WHERE constatId = :constatId and propertyId in (:ids)")
    fun deleteConstatPropertyRefByIds(constatId: String, ids: List<String>)

    @Query("DELETE FROM CONSTATOWNERCROSSREF WHERE constatId = :constatId and ownerId in (:ids)")
    fun deleteConstatOwnerRefByIds(constatId: String, ids: List<String>)

    @Query("DELETE FROM CONSTATCONTRACTORCROSSREF WHERE constatId = :constatId and contractorId in (:ids)")
    fun deleteConstatContractorRefByIds(constatId: String, ids: List<String>)

    @Query("DELETE FROM CONSTATTENANTCROSSREF WHERE constatId = :constatId and tenantId in (:ids)")
    fun deleteConstatTenantRefByIds(constatId: String, ids: List<String>)

    @Delete
    fun deleteConstatAgency(constatAgencyCrossRef: ConstatAgencyCrossRef)

    @Delete
    fun deleteConstatUser(constatUsersCrossRef: ConstatUsersCrossRef)
}