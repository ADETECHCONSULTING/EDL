package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.DetailDao
import fr.atraore.edl.data.dao.TenantDao
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.Tenant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val detailDao: DetailDao
) : BaseRepository<Detail>(detailDao) {

    val allDetails: Flow<List<Detail>> = detailDao.getAllDetails()

    fun getDetailById(id: String): Flow<Detail> = detailDao.getDetailById(id)
}