package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import fr.atraore.edl.data.models.data.RoomWithElements
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.utils.ELEMENT_REFERENCE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementReferenceDao : BaseDao<ElementReference> {
    //** GET **
    //Select all RoomReference
    @Query("SELECT * FROM $ELEMENT_REFERENCE_TABLE")
    fun getAllElementReference(): Flow<List<ElementReference>>

    @Query("SELECT * FROM $ELEMENT_REFERENCE_TABLE WHERE name LIKE :searchQuery")
    fun searchElementQuery(searchQuery: String) : Flow<List<ElementReference>>

    @Transaction
    @Query("SELECT * FROM ElementReference e JOIN RoomElementCrossRef rel ON rel.elementReferenceId = e.elementReferenceId AND rel.roomReferenceId = :roomId")
    fun getElementsRefWhereRoomId(roomId: String) : Flow<List<ElementReference>>
}