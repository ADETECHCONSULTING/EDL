package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.Compteur
import kotlinx.coroutines.flow.Flow

@Dao
interface CompteurDao : BaseDao<Compteur> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM Compteur")
    fun getAll(): Flow<List<Compteur>>

    @Query("SELECT * FROM Compteur WHERE constat_id =:idConstat and compteur_ref_id = :idCompteurRef")
    fun getById(idConstat: String, idCompteurRef: Int): Flow<Compteur>

    @Query("UPDATE Compteur SET image_path =:imagePath WHERE compteur_ref_id =:idCompteurRef")
    suspend fun updateImagePath(imagePath: String, idCompteurRef: Int)

    @Query("UPDATE Compteur SET image_path_second =:imagePath WHERE compteur_ref_id =:idCompteurRef")
    suspend fun updateImagePathSecond(imagePath: String, idCompteurRef: Int)
}