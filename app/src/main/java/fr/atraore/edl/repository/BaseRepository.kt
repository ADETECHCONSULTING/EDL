package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.BaseDao
import fr.atraore.edl.data.models.Contractor

abstract class BaseRepository<T>(
    private val dao: BaseDao<T>
) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun save(obj: T) {
        dao.save(obj)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveList(objs: List<T>) {
        dao.saveList(objs)
    }
}