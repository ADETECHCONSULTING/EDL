package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.BaseDao

open class BaseRepository<T>(
    private val dao: BaseDao<T>
) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun save(vararg obj: T) {
        dao.save(*obj)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveList(objs: List<T>) {
        dao.saveList(objs)
    }
}