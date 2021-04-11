package fr.atraore.edl.ui.edl.search.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.TenantRepository

class TenantSearchViewModel(
    private val repository: TenantRepository,
    private val constatRepository: ConstatRepository,
) : ViewModel() {
    val allTenants: LiveData<List<Tenant>> = repository.allTenants.asLiveData()

    suspend fun saveConstatTenant(constatId: String, tenantId: String) {
        constatRepository.saveConstatTenantCrossRef(constatId, tenantId)
    }
}

class TenantSearchViewModelFactory(
    private val repository: TenantRepository,
    private val constatRepository: ConstatRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TenantSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TenantSearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}