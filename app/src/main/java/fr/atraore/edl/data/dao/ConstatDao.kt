package fr.atraore.edl.data.dao

import androidx.lifecycle.LiveData
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


    //** UPDATE **
    @Update
    fun update(constat: Constat)

    //** DELETE **
    @Delete
    fun delete(constat: Constat)

    @Query("DELETE FROM $CONSTAT_TABLE")
    fun deleteAll()
}