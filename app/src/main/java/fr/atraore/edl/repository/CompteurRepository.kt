package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.CompteurDao
import fr.atraore.edl.data.models.entity.Compteur
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompteurRepository @Inject constructor(
    private val dao: CompteurDao
) : BaseRepository<Compteur>(dao) {

    fun getAll(): Flow<List<Compteur>> = dao.getAll()
    fun getById(idConstat: String, compteurRefId: Int): Flow<Compteur?> =
        dao.getById(idConstat, compteurRefId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveArgsNullable(vararg obj: Compteur?) {
        dao.saveList(obj.filterNotNull())
    }

    suspend fun saveImagePath(compteurRefId: Int, imagePath: String, isSecond: Boolean) {
        if (isSecond) {
            dao.updateImagePathSecond(imagePath, compteurRefId)
        } else {
            dao.updateImagePath(imagePath, compteurRefId)
        }
    }
}