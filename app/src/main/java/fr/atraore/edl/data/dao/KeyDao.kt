package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.KeyReference
import fr.atraore.edl.utils.KEY_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyDao : BaseDao<KeyReference> {
    //** GET **
    //Select all 
    @Query("SELECT * FROM $KEY_TABLE")
    fun getAll(): Flow<List<KeyReference>>

    @Query("SELECT * FROM $KEY_TABLE WHERE actif = 1")
    fun getAllActifKeysRef(): Flow<List<KeyReference>>

    @Query("SELECT * FROM $KEY_TABLE WHERE id =:id")
    fun getById(id: String): Flow<KeyReference>

    @Query("SELECT * FROM $KEY_TABLE WHERE name LIKE :searchQuery")
    fun searchKeysQuery(searchQuery: String) : Flow<List<KeyReference>>
}