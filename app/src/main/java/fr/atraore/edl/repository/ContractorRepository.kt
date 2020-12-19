package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ContractorDao
import fr.atraore.edl.data.models.Contractor
import kotlinx.coroutines.flow.Flow

class ContractorRepository(
    private val contractorDao: ContractorDao
) : BaseRepository<Contractor>(contractorDao) {
    val allContractors: Flow<List<Contractor>> = contractorDao.getAllContractors()
}