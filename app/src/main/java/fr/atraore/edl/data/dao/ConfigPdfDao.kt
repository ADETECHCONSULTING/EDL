package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.ConfigPdf
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigPdfDao : BaseDao<ConfigPdf> {
    //** GET **
    //Select first
    @Query("SELECT * FROM ConfigPdf LIMIT 1")
    fun getFirst(): Flow<ConfigPdf>

    @Query("SELECT * FROM ConfigPdf WHERE id =:id")
    fun getById(id: String): Flow<ConfigPdf>

}