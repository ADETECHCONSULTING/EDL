package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.DetailDao
import fr.atraore.edl.data.models.entity.Detail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val detailDao: DetailDao
) : BaseRepository<Detail>(detailDao) {

    val allDetails: Flow<List<Detail>> = detailDao.getAllDetails()
    fun detailsByIdRoomAndIdConstat(idRoom: String, idConstat: String): Flow<List<Detail>> = detailDao.getDetailsByIdRoomAndIdConstat(idRoom, idConstat)

    fun getDetailById(id: String): Flow<Detail> = detailDao.getDetailById(id)

    suspend fun updateEtat(etat: String, idDetail: String) = detailDao.updateEtat(etat, idDetail)
    suspend fun updateProprete(propre: String, idDetail: String) = detailDao.updateProprete(propre, idDetail)
    suspend fun deleteAllDetailsFromRoom(idRoom: String) = detailDao.deleteAllDetailsFromRoom(idRoom)
}