package fr.atraore.edl.ui.edl.search.biens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.PropertyRepository
import javax.inject.Inject

@HiltViewModel
class PropertySearchViewModel @Inject constructor(
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