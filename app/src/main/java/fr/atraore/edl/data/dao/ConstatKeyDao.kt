package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.ConstatKey
import fr.atraore.edl.utils.CONSTAT_KEY_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstatKeyDao : BaseDao<ConstatKey> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM $CONSTAT_KEY_TABLE")
    fun getAll(): Flow<List<ConstatKey>>

    @Query("SELECT * FROM $CONSTAT_KEY_TABLE WHERE id =:id")
    fun getById(id: String): Flow<ConstatKey>

    @Query("SELECT * FROM $CONSTAT_KEY_TABLE WHERE constatId = :constatId")
    fun getAllFromConstat(constatId: String): Flow<List<ConstatKey>>
}