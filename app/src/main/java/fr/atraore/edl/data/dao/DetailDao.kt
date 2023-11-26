package fr.atraore.edl.data.dao

import android.telecom.Call.Details
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

    @Query("SELECT * FROM $DETAIL_TABLE WHERE idEqp =:id AND idConstat = :idConstat AND idLot = :idLot")
    fun getDetailByIdEqp(id: String, idConstat: String, idLot: Int): Flow<Detail>

    @Query("SELECT * FROM $DETAIL_TABLE WHERE idDetail =:id")
    fun getDetailById(id: String): Flow<Detail>

    @Query("UPDATE Detail SET etat =:etat WHERE idDetail =:idDetail")
    suspend fun updateEtat(etat: String, idDetail: String)

    @Query("UPDATE Detail SET proprete =:proprete WHERE idDetail =:idDetail")
    suspend fun updateProprete(proprete: String, idDetail: String)

    @Query("UPDATE Detail SET fonctionmt =:fonctionnement WHERE idDetail =:idDetail")
    suspend fun updateFonctionnement(fonctionnement: String, idDetail: String)

    @Query("UPDATE Detail SET imagesPaths =:imagesPaths WHERE idDetail =:idDetail")
    suspend fun updateImagesPaths(imagesPaths: String, idDetail: String)

    @Transaction
    @Query("SELECT * FROM RoomReference r JOIN DETAIL dt ON dt.idRoomRef = id WHERE dt.idConstat = :id ORDER BY r.name asc")
    fun getRoomDetails(id: String) : Flow<Map<RoomReference, List<Detail>>>

    @Query("SELECT * FROM Detail dt WHERE dt.idEqp = :roomId AND dt.idEqp = :elementId AND dt.idLot = :idLot ORDER BY dt.intitule asc")
    fun getDetailsByRoomRefElementIdIdLot(roomId: String, elementId: String, idLot: Int) : Flow<List<Detail>>

    @Query("SELECT * FROM Detail dt WHERE dt.idKey = :idKey AND dt.idConstat = :constatId ORDER BY dt.idDetail desc LIMIT 1")
    fun getDetailByIdKeyAndConstat(idKey: Int, constatId: String): Flow<Detail>

    @Query("SELECT * FROM Detail dt WHERE dt.idOutDoorEqpt = :idOutdoor AND dt.idConstat = :constatId ORDER BY dt.idDetail desc LIMIT 1")
    fun getDetailByIdOutdoorAndConstat(idOutdoor: Int, constatId: String): Flow<Detail>
}