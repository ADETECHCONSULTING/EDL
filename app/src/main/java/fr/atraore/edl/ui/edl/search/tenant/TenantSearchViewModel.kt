package fr.atraore.edl.ui.edl.search.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.repository.PropertyRepository
import fr.atraore.edl.repository.TenantRepository
import fr.atraore.edl.ui.edl.search.biens.PropertySearchViewModel

class TenantSearchViewModel(
    private val repository: TenantRepository
) : ViewModel() {
    val allTenants: LiveData<List<Tenant>> = repository.allTenants.asLiveData()
}

class TenantSearchViewModelFactory(
    private val repository: TenantRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TenantSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TenantSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}