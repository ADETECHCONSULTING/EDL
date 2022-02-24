package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.PhotoCompteur
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoCompteurDao : BaseDao<PhotoCompteur> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM PhotoCompteur")
    fun getAll(): Flow<List<PhotoCompteur>>

    @Query("SELECT * FROM PhotoCompteur WHERE id =:id")
    fun getById(id: String): Flow<PhotoCompteur>

}