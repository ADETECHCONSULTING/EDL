package fr.atraore.edl.ui.edl.search.owner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.Owner
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.OwnerRepository
import javax.inject.Inject

@HiltViewModel
class OwnerSearchViewModel @Inject constructor(
    private val repository: OwnerRepository,
    private val constatRepository: ConstatRepository,
) : ViewModel() {
    val allOwners: LiveData<List<Owner>> = repository.allOwners.asLiveData()

    suspend fun saveConstatOwner(constatId: String, ownerId: String) {
        constatRepository.saveConstatOwnerCrossRef(constatId, ownerId)
    }

    suspend fun deleteConstatOwner(constatId: String, ownerId: String) {
        constatRepository.deleteConstatOwnerCrossRef(constatId, ownerId)
    }
}