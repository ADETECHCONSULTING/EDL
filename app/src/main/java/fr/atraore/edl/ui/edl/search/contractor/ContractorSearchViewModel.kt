package fr.atraore.edl.ui.edl.search.contractor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.ContractorRepository

class ContractorSearchViewModel(
    private val repository: ContractorRepository,
    private val constatRepository: ConstatRepository
) : ViewModel() {
    val allContractors: LiveData<List<Contractor>> = repository.allContractors.asLiveData()

    suspend fun saveConstatContractor(constatId: String, contractorId: String) {
        constatRepository.saveConstatContractorCrossRef(constatId, contractorId)
    }

    suspend fun deleteConstatContractor(constatId: String, contractorId: String) {
        constatRepository.deleteConstatContractorCrossRef(constatId, contractorId)
    }
}

class ContractorSearchViewModelFactory(
    private val repository: ContractorRepository,
    private val constatRepository: ConstatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractorSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractorSearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}