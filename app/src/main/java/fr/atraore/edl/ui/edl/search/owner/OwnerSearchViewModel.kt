package fr.atraore.edl.ui.edl.search.owner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.repository.OwnerRepository

class OwnerSearchViewModel(
    private val repository: OwnerRepository
) : ViewModel() {
    val allOwners: LiveData<List<Owner>> = repository.allOwners.asLiveData()

}

class OwnerSearchViewModelFactory(
    private val repository: OwnerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnerSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OwnerSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}