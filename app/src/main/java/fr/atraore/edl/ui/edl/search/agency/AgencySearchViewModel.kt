package fr.atraore.edl.ui.edl.search.agency

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.repository.AgencyRepository
import fr.atraore.edl.repository.ConstatRepository
import javax.inject.Inject

@HiltViewModel
class AgencySearchViewModel @Inject constructor(
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


open class Fruit(val name: String = "Fruit")
class Apple: Fruit("")