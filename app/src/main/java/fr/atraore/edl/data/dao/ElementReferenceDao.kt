package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.utils.ELEMENT_REFERENCE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementReferenceDao : BaseDao<ElementReference> {
    //** GET **
    //Select all RoomReference
    @Query("SELECT * FROM $ELEMENT_REFERENCE_TABLE")
    fun getAllElementReference(): Flow<List<ElementReference>>

}