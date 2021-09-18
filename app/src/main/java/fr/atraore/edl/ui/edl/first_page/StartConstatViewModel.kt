package fr.atraore.edl.ui.edl.first_page

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.*
import fr.atraore.edl.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StartConstatViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository,
    val contractorRepository: ContractorRepository,
    @Assisted val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): StartConstatViewModel
    }

    //TODO Update methods
    fun saveConstat(constat: Constat) = viewModelScope.launch {
        repository.save(constat)
    }

    fun saveTenants(tenants: List<Tenant>) = viewModelScope.launch {
        tenantRepository.saveList(tenants)
    }

    fun saveOwners(owners: List<Owner>) = viewModelScope.launch {
        ownerRepository.saveList(owners)
    }

    fun saveProperties(properties: List<Property>) = viewModelScope.launch {
        propertyRepository.saveList(properties)
    }

    fun saveContractor(contractors: List<Contractor>) = viewModelScope.launch {
        contractorRepository.saveList(contractors)
    }
}