package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.utils.DETAIL_TABLE
import fr.atraore.edl.utils.TENANT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao : BaseDao<Detail> {
    //** GET **
    //Select all detail
    @Query("SELECT * FROM $DETAIL_TABLE")
    fun getAllDetails(): Flow<List<Detail>>

    @Query("SELECT * FROM $DETAIL_TABLE WHERE idDetail =:id")
    fun getDetailById(id: String): Flow<Detail>

    @Query("SELECT * FROM $DETAIL_TABLE WHERE idRoom =:idRoom and idConstat = :idConstat")
    fun getDetailsByIdRoomAndIdConstat(idRoom: String, idConstat: String): Flow<List<Detail>>

    @Query("UPDATE Detail SET etat =:etat WHERE idDetail =:idDetail")
    suspend fun updateEtat(etat: String, idDetail: String)

    @Query("DELETE FROM Detail WHERE idRoom =:idRoom")
    suspend fun deleteAllDetailsFromRoom(idRoom: String)
}