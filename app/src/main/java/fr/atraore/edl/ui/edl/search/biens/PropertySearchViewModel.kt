package fr.atraore.edl.ui.edl.search.biens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.PropertyRepository

class PropertySearchViewModel(
    private val repository: PropertyRepository,
    private val constatRepository: ConstatRepository
) : ViewModel() {
    val allProperties: LiveData<List<Property>> = repository.allProperties.asLiveData()

    suspend fun saveConstatProperty(constatId: String, propertyId: String) {
        constatRepository.saveConstatPropertyCrossRef(constatId, propertyId)
    }

    suspend fun deleteConstatProperty(constatId: String, propertyId: String) {
        constatRepository.deleteConstatPropertyCrossRef(constatId, propertyId)
    }
}

class PropertySearchViewModelFactory(
    private val repository: PropertyRepository,
    private val constatRepository: ConstatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertySearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PropertySearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}