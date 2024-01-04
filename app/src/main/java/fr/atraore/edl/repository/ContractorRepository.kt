package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ContractorDao
import fr.atraore.edl.data.models.entity.Contractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContractorRepository @Inject constructor(
    private val contractorDao: ContractorDao
) : BaseRepository<Contractor>(contractorDao) {
    val allContractors: Flow<List<Contractor>> = contractorDao.getAllContractors()
    fun getById(id: String): Flow<Contractor> = contractorDao.getById(id)


}