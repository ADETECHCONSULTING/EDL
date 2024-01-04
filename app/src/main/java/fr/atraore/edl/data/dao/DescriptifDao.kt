package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.Descriptif
import kotlinx.coroutines.flow.Flow

@Dao
interface DescriptifDao : BaseDao<Descriptif> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM Descriptif")
    fun getAll(): Flow<List<Descriptif>>

    @Query("SELECT * FROM Descriptif WHERE id =:id")
    fun getById(id: String): Flow<Descriptif>

}