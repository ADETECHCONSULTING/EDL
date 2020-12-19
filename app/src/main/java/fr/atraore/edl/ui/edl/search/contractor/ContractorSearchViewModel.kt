package fr.atraore.edl.ui.edl.search.contractor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.repository.ContractorRepository

class ContractorSearchViewModel(
    private val repository: ContractorRepository
) : ViewModel() {
    val allContractors: LiveData<List<Contractor>> = repository.allContractors.asLiveData()
}

class ContractorSearchViewModelFactory(
    private val repository: ContractorRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractorSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractorSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}