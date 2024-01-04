package fr.atraore.edl.ui.edl.search.contractor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.Contractor
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.ContractorRepository
import javax.inject.Inject

@HiltViewModel
class ContractorSearchViewModel @Inject constructor(
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