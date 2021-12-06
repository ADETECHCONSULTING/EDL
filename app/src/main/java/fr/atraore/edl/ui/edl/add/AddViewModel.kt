package fr.atraore.edl.ui.edl.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.repository.*
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val contractorRepository: ContractorRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository
) : ViewModel() {

    val tenant: MutableLiveData<Tenant> = MutableLiveData()
    val contractor: MutableLiveData<Contractor> = MutableLiveData()
    val owner: MutableLiveData<Owner> = MutableLiveData()
    val property: MutableLiveData<Property> = MutableLiveData()

    val constatHeaderInfo = MutableLiveData<String>()
    fun getTenantById(objectId: String): LiveData<Tenant?> = tenantRepository.getById(objectId).asLiveData()
    fun getContractorById(objectId: String): LiveData<Contractor?>  = contractorRepository.getById(objectId).asLiveData()
    fun getOwnerById(objectId: String): LiveData<Owner?>  = ownerRepository.getById(objectId).asLiveData()
    fun getPropertyById(objectId: String): LiveData<Property?>  = propertyRepository.getById(objectId).asLiveData()
}