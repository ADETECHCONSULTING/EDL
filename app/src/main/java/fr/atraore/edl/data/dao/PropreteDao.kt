package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.Proprete
import kotlinx.coroutines.flow.Flow

@Dao
interface PropreteDao : BaseDao<Proprete> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM Proprete")
    fun getAll(): Flow<List<Proprete>>

    @Query("SELECT * FROM Proprete WHERE id =:id")
    fun getById(id: String): Flow<Proprete>

}