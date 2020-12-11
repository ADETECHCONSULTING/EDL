package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.ConstatDao
import fr.atraore.edl.data.models.Constat
import kotlinx.coroutines.flow.Flow

class ConstatRepository(
    private val constatDao: ConstatDao
) {
    val allConstats: Flow<List<Constat>> = constatDao.getAllConstat()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun save(constat: Constat) {
        constatDao.save(constat)
    }
}