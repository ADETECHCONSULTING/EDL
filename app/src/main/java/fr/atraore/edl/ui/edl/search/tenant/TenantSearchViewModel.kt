package fr.atraore.edl.ui.edl.search.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.Tenant
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.TenantRepository
import javax.inject.Inject

@HiltViewModel
class TenantSearchViewModel @Inject constructor(
    private val repository: TenantRepository,
    private val constatRepository: ConstatRepository,
) : ViewModel() {
    val allTenants: LiveData<List<Tenant>> = repository.allTenants.asLiveData()

    suspend fun saveConstatTenant(constatId: String, tenantId: String) {
        constatRepository.saveConstatTenantCrossRef(constatId, tenantId)
    }

    suspend fun deleteConstatTenant(constatId: String, tenantId: String) {
        constatRepository.deleteConstatTenantCrossRef(constatId, tenantId)
    }
}