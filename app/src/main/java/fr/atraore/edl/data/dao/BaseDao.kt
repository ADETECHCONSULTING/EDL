package fr.atraore.edl.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.utils.PROPERTY_TABLE
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {
    //** INSERT **
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg obj: T)

    //** UPDATE **
    @Update
    fun update(obj: T)

    @Update
    suspend fun saveList(objs: List<T>)
}