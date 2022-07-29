package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.utils.DETAIL_TABLE
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

    @Query("UPDATE Detail SET proprete =:proprete WHERE idDetail =:idDetail")
    suspend fun updateProprete(proprete: String, idDetail: String)

    @Query("UPDATE Detail SET fonctionmt =:fonctionnement WHERE idDetail =:idDetail")
    suspend fun updateFonctionnement(fonctionnement: String, idDetail: String)

    @Query("UPDATE Detail SET imagesPaths =:imagesPaths WHERE idDetail =:idDetail")
    suspend fun updateImagesPaths(imagesPaths: String, idDetail: String)

    @Query("DELETE FROM Detail WHERE idRoom =:idRoom")
    suspend fun deleteAllDetailsFromRoom(idRoom: String)

    @Transaction
    @Query("SELECT * FROM RoomReference r JOIN DETAIL dt ON dt.idRoom = roomReferenceId WHERE dt.idConstat = :id ORDER BY r.name asc")
    fun getRoomDetails(id: String) : Flow<Map<RoomReference, List<Detail>>>

    @Query("SELECT * FROM Detail dt WHERE dt.idRoom = :roomId AND dt.idElement = :elementId AND dt.idLot = :idLot ORDER BY dt.intitule asc")
    fun getDetailsByRoomRefElementIdIdLot(roomId: String, elementId: String, idLot: Int) : Flow<List<Detail>>
}