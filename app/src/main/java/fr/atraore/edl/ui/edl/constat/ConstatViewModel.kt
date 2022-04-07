package fr.atraore.edl.ui.edl.constat

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.*
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
    val detailRepository: DetailRepository,
    val lotRepository: LotRepository,
    val constatKeyRepository: ConstatKeyRepository,
    val keyRepository: KeyRepository,
    @Assisted val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val firstRoomReference: LiveData<RoomReference> = roomRepository.firstRoomReference().asLiveData()
    val allRoomReference: LiveData<List<RoomReference>> = roomRepository.allRoomReferences().asLiveData()
    val allElementReference: LiveData<List<ElementReference>> = elementRepository.allElementReference().asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    fun getRoomWithDetails(idLot: Int) : LiveData<Map<RoomReference, List<Detail>>> = roomRepository.getRoomDetails(idLot).asLiveData()
    fun getDetailsByIdRoomAndIdConstat(idRoom: String, idConstat: String) : LiveData<List<Detail>> = detailRepository.detailsByIdRoomAndIdConstat(idRoom, idConstat).asLiveData()
    //combined live data
    val initFirstRoomReference = CombinedLiveData(firstRoomReference, allElementReference)
    fun roomCombinedLiveData(idLot: Int) = TripleCombinedLiveData(getRoomWithDetails(idLot), allRoomReference, allElementReference)
    val getAllLotReference : LiveData<List<LotReference>> = lotRepository.getAll().asLiveData()
    fun allKeysFromConstat(constatId: String) : LiveData<List<ConstatKey>> = constatKeyRepository.getAllFromConstat(constatId).asLiveData()
    fun allActifKeysRef() : LiveData<List<KeyReference>> = keyRepository.getAllActifKeysRef().asLiveData()

    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): ConstatViewModel
    }

    suspend fun saveConstatRoomCrossRef(constatId: String, roomId: String) {
        repository.saveConstatRoomCrossRef(constatId, roomId)
    }

    suspend fun saveDetail(detail: Detail) {
        detailRepository.save(detail)
    }

    suspend fun saveRoomDetailCrossRef(roomId: String, detailId: String) {
        repository.saveRoomDetailCrossRef(roomId, detailId)
    }

    fun saveConstat(constat: Constat) = viewModelScope.launch {
        repository.save(constat)
    }

    fun saveProcuration(constatId: String, procuration: String) = viewModelScope.launch {
        if (procuration.isNotEmpty()) {
            repository.saveProcuration(constatId, procuration)
        }
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

    suspend fun deleteRoomDetailCrossRef(roomId: String, detailId: String) {
        repository.deleteRoomDetailCrossRef(roomId, detailId)
    }

    suspend fun deleteConstatRoomCrossRef(constatId: String, roomId: String) {
        repository.deleteConstatRoomCrossRef(constatId, roomId)
    }

    suspend fun deleteAllDetailsFromRoom(roomId: String) {
        detailRepository.deleteAllDetailsFromRoom(roomId)
    }

    suspend fun deleteConstatPropertyCrossRefByIds() {
        constatDetail.value?.let { constatWithDetails ->
            viewModelScope.launch {
                constatWithDetails.properties.map { property -> property.propertyId }.forEach {
                    repository.deleteConstatPropertyCrossRef(constatWithDetails.constat.constatId, it)
                }
            }
        }
    }

    fun deleteConstatOwnerCrossRefByIds() {
        constatDetail.value?.let { constatWithDetails ->
            viewModelScope.launch {
                constatWithDetails.owners.map { owner -> owner.ownerId }.forEach {
                    repository.deleteConstatOwnerCrossRef(constatWithDetails.constat.constatId, it)
                }
            }
        }
    }

    suspend fun deleteConstatTenantCrossRefByIds() {
        constatDetail.value?.let { constatWithDetails ->
            viewModelScope.launch {
                constatWithDetails.tenants.map { tenant -> tenant.tenantId }.forEach {
                    repository.deleteConstatTenantCrossRef(constatWithDetails.constat.constatId, it)
                }
            }
        }
    }

    suspend fun deleteConstatContractorCrossRefByIds() {
        constatDetail.value?.let { constatWithDetails ->
            viewModelScope.launch {
                constatWithDetails.contractors.map { contractor -> contractor.contractorId }.forEach {
                    repository.deleteConstatContractorCrossRef(constatWithDetails.constat.constatId, it)
                }
            }
        }
    }
}