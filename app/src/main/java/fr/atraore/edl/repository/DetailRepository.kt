package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.DetailDao
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.EquipmentReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val detailDao: DetailDao
) : BaseRepository<Detail>(detailDao) {
    fun getDetailByIdEqp(idEqp: String, idConstat: String, idLot: Int): Flow<Detail> = detailDao.getDetailByIdEqp(idEqp, idConstat, idLot)
    fun getDetailById(id: String): Flow<Detail> = detailDao.getDetailById(id)

    suspend fun updateEtat(etat: String, idDetail: String) = detailDao.updateEtat(etat, idDetail)
    suspend fun updateProprete(propre: String, idDetail: String) = detailDao.updateProprete(propre, idDetail)
    suspend fun updateFonctionnement(fonctionnement: String, idDetail: String) = detailDao.updateFonctionnement(fonctionnement, idDetail)
    suspend fun updateImagesPaths(imagesPaths: String, idDetail: String) = detailDao.updateImagesPaths(imagesPaths, idDetail)

    fun getRoomDetails(idConstat: String) : Flow<Map<RoomReference, List<Detail>>> = detailDao.getRoomDetails(idConstat)
    fun getDetailByIdKeyAndConstat(idKey: Int, constatId: String): Flow<Detail> = detailDao.getDetailByIdKeyAndConstat(idKey, constatId)
    fun getDetailByIdOutdoorAndConstat(idOutdoorEquipement: Int, constatId: String): Flow<Detail> = detailDao.getDetailByIdOutdoorAndConstat(idOutdoorEquipement, constatId)
}