package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.CompteurReference
import kotlinx.coroutines.flow.Flow

@Dao
interface CompteurReferenceDao : BaseDao<CompteurReference> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM CompteurReference")
    fun getAll(): Flow<List<CompteurReference>>

    @Query("SELECT * FROM CompteurReference WHERE id =:id")
    fun getById(id: String): Flow<CompteurReference>

}