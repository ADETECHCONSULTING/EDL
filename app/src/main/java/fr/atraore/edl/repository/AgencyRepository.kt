package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.AgencyDao
import fr.atraore.edl.data.models.entity.Agency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AgencyRepository @Inject constructor(
    private val agencyDao: AgencyDao
) : BaseRepository<Agency>(agencyDao) {

    val allAgency: Flow<List<Agency>> = agencyDao.getAllAgencies()
    fun getById(id: String): Flow<Agency> = agencyDao.getById(id)
}