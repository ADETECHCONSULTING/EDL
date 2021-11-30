package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.LotReference
import fr.atraore.edl.utils.LOT_REFERENCE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface LotReferenceDao : BaseDao<LotReference> {
    //** GET **
    //Select all RoomReference
    @Query("SELECT * FROM $LOT_REFERENCE_TABLE")
    fun getAllLotReference(): Flow<List<LotReference>>

    @Query("SELECT * FROM $LOT_REFERENCE_TABLE WHERE mandatory = 1")
    fun getAllMandatoryLotReference(): Flow<List<LotReference>>
}