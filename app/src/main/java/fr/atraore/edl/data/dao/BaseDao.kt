package fr.atraore.edl.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    //** INSERT **
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg obj: T)

    //** UPDATE **
    @Update
    fun update(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(objs: List<T>)

}