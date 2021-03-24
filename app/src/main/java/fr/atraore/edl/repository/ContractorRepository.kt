package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.ContractorDao
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.data.models.Owner
import kotlinx.coroutines.flow.Flow

class ContractorRepository(
    private val contractorDao: ContractorDao
) : BaseRepository<Contractor>(contractorDao) {
    val allContractors: Flow<List<Contractor>> = contractorDao.getAllContractors()


}