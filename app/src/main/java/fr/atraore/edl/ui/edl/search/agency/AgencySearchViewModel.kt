package fr.atraore.edl.ui.edl.search.agency

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.repository.AgencyRepository
import fr.atraore.edl.repository.ConstatRepository

class AgencySearchViewModel(
    private val repository: AgencyRepository,
    private val constatRepository: ConstatRepository
): ViewModel() {
    val allAgencies: LiveData<List<Agency>> = repository.allAgency.asLiveData()

    suspend fun updateExistingAgency(constat: Constat, agency: Agency) {
        constatRepository.updateExistingAgencyCrossRef(constat.constatId, agency.agencyId)
    }

    suspend fun save(constat: Constat, agency: Agency) {
        constatRepository.saveConstatAgencyCrossRef(constat.constatId, agency.agencyId)
    }
}

class AgencySearchViewModelFactory(
    private val repository: AgencyRepository,
    private val constatRepository: ConstatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgencySearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgencySearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}