package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.AgencyDao
import fr.atraore.edl.data.models.Agency
import kotlinx.coroutines.flow.Flow

class AgencyRepository(
    private val agencyDao: AgencyDao
) : BaseRepository<Agency>(agencyDao) {

    val allAgency: Flow<List<Agency>> = agencyDao.getAllAgencies()

}