package fr.atraore.edl.ui.edl.start

import androidx.lifecycle.*
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.TenantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StartConstatViewModel(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

    //TODO Update methods
    fun saveConstat(constat: Constat) = viewModelScope.launch {
        repository.save(constat)
    }

    fun saveTenant(tenant: Tenant) = viewModelScope.launch {
        tenantRepository.save(tenant)
    }
}

class StartConstatViewModelFactory(
    private val repository: ConstatRepository,
    private val tenantRepository: TenantRepository,
    private val constatId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartConstatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartConstatViewModel(repository, tenantRepository, constatId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}