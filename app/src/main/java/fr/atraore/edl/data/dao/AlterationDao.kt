package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.Alteration
import fr.atraore.edl.utils.ALTERATION_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface AlterationDao : BaseDao<Alteration> {
    //** GET **
    //Select all detail
    @Query("SELECT * FROM $ALTERATION_TABLE")
    fun getAll(): Flow<List<Alteration>>

    @Query("SELECT * FROM $ALTERATION_TABLE WHERE id =:id")
    fun getById(id: String): Flow<Alteration>

}