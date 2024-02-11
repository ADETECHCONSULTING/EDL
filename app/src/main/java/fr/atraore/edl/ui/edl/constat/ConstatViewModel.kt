package fr.atraore.edl.ui.edl.constat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.crossRef.RoomConstatCrossRef
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Contractor
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.KeyReference
import fr.atraore.edl.data.models.entity.OutdoorEquipementReference
import fr.atraore.edl.data.models.entity.Owner
import fr.atraore.edl.data.models.entity.Property
import fr.atraore.edl.data.models.entity.Tenant
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.ContractorRepository
import fr.atraore.edl.repository.DetailRepository
import fr.atraore.edl.repository.EquipmentRepository
import fr.atraore.edl.repository.KeyRepository
import fr.atraore.edl.repository.OutdoorEquipementRepository
import fr.atraore.edl.repository.OwnerRepository
import fr.atraore.edl.repository.PropertyRepository
import fr.atraore.edl.repository.RoomRepository
import fr.atraore.edl.repository.TenantRepository
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
    val detailRepository: DetailRepository,
    val keyRepository: KeyRepository,
    val outdoorRepository: OutdoorEquipementRepository,
    val equipmentRepository: EquipmentRepository,
    @Assisted val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    fun getDetailByIdEqp(idEqp: String, idConstat: String, idLot: Int): LiveData<Detail> = detailRepository.getDetailByIdEqp(idEqp, idConstat, idLot).asLiveData()

    fun getDetailByIdKeyAndConstat(idKey: Int, constatId: String) = detailRepository.getDetailByIdKeyAndConstat(idKey, constatId).asLiveData()
    fun getDetailByIdOutdoorAndConstat(idOutdoorEquipement: Int, constatId: String) = detailRepository.getDetailByIdOutdoorAndConstat(idOutdoorEquipement, constatId).asLiveData()
    fun allActifKeysRef() : LiveData<List<KeyReference>> = keyRepository.getAllActifKeysRef().asLiveData()
    fun allActifOutdoorRef() : LiveData<List<OutdoorEquipementReference>> = outdoorRepository.getAllActifRef().asLiveData()

    val getAllEquipments = equipmentRepository.getAllEquipments().asLiveData()
    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO


    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): ConstatViewModel
    }
    suspend fun saveDetail(detail: Detail) {
        detailRepository.save(detail)
    }

    suspend fun saveEquipmentRef(itemId: String, name: String, idRoomRef: Int) {
        equipmentRepository.updateEquipmentReference(itemId, name, idRoomRef)
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

    suspend fun deleteEquipmentRef(itemId: String) {
        equipmentRepository.deleteEquipmentRef(itemId)
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

    suspend fun saveRoomConstatCrossRef(crossRefs: List<RoomConstatCrossRef>) {
        viewModelScope.launch {
            roomRepository.saveRoomConstatCrossRef(crossRefs)
        }
    }
}