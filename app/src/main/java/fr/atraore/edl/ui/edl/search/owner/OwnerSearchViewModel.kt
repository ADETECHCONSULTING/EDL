package fr.atraore.edl.ui.edl.search.owner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.OwnerRepository

class OwnerSearchViewModel(
    private val repository: OwnerRepository,
    private val constatRepository: ConstatRepository,
) : ViewModel() {
    val allOwners: LiveData<List<Owner>> = repository.allOwners.asLiveData()

    suspend fun saveConstatOwner(constatId: String, ownerId: String) {
        constatRepository.saveConstatOwnerCrossRef(constatId, ownerId)
    }
}

class OwnerSearchViewModelFactory(
    private val repository: OwnerRepository,
    private val constatRepository: ConstatRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnerSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OwnerSearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}