package fr.atraore.edl.ui.edl.constat

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.data.ElementWithRefsEtat
import fr.atraore.edl.repository.*
import fr.atraore.edl.utils.CombinedLiveData
import fr.atraore.edl.utils.TripleCombinedLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ConstatViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository,
    val contractorRepository: ContractorRepository,
    val roomRepository: RoomRepository,
    val elementRepository: ElementRepository,
    @Assisted val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val firstRoomReference: LiveData<RoomReference> = roomRepository.firstRoomReference().asLiveData()
    val allRoomReference: LiveData<List<RoomReference>> = roomRepository.allRoomReferences().asLiveData()
    val allElementReference: LiveData<List<ElementReference>> = elementRepository.allElementReference().asLiveData()
    val allElementsWithRefsEtat : LiveData<List<ElementWithRefsEtat>> = elementRepository.allElementsWithRefsEtat().asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    fun getRoomWithElements() : LiveData<List<RoomWithElements>> = roomRepository.getRoomDetails().asLiveData()
    //combined live data
    val combinedLiveData = TripleCombinedLiveData(constatDetail, firstRoomReference, allElementReference)
    fun roomCombinedLiveData() = TripleCombinedLiveData(getRoomWithElements(), allRoomReference, allElementReference)

    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): ConstatViewModel
    }

    suspend fun saveConstatRoomCrossRef(constatId: String, roomId: String) {
        repository.saveConstatRoomCrossRef(constatId, roomId)
    }

    suspend fun saveRoomElementCrossRef(roomId: String, elementId: String, rename: String?) {
        repository.saveRoomElementCrossRef(roomId, elementId, rename)
    }

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

    suspend fun deleteRoomElementCrossRef(roomId: String, elementId: String) {
        repository.deleteRoomElementCrossRef(roomId, elementId)
    }
}