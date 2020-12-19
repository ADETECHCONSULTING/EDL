package fr.atraore.edl.ui.edl.search.agency

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.repository.AgencyRepository

class AgencySearchViewModel(
    private val repository: AgencyRepository
): ViewModel() {
    val allAgencies: LiveData<List<Agency>> = repository.allAgency.asLiveData()
}

class AgencySearchViewModelFactory(
    private val repository: AgencyRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgencySearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgencySearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}