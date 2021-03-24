package fr.atraore.edl.ui.edl.start

import androidx.lifecycle.*
import fr.atraore.edl.data.models.*
import fr.atraore.edl.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StartConstatViewModel(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository,
    val contractorRepository: ContractorRepository,
    val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

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

class StartConstatViewModelFactory(
    private val repository: ConstatRepository,
    private val tenantRepository: TenantRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository,
    val contractorRepository: ContractorRepository,
    private val constatId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartConstatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartConstatViewModel(repository, tenantRepository, ownerRepository, propertyRepository, contractorRepository, constatId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}