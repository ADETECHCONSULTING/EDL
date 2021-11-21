package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.Etat
import kotlinx.coroutines.flow.Flow

@Dao
interface EtatDao : BaseDao<Etat> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM Etat")
    fun getAll(): Flow<List<Etat>>

    @Query("SELECT * FROM Etat WHERE id =:id")
    fun getById(id: String): Flow<Etat>

}