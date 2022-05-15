package fr.atraore.edl.ui.edl.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.repository.*
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    val repository: ConstatRepository,
    val tenantRepository: TenantRepository,
    val contractorRepository: ContractorRepository,
    val ownerRepository: OwnerRepository,
    val propertyRepository: PropertyRepository,
    val userRepository: UserRepository,
    val agencyRepository: AgencyRepository
) : ViewModel() {

    val tenant: MutableLiveData<Tenant> = MutableLiveData()
    val contractor: MutableLiveData<Contractor> = MutableLiveData()
    val owner: MutableLiveData<Owner> = MutableLiveData()
    val property: MutableLiveData<Property> = MutableLiveData()
    val user: MutableLiveData<Users> = MutableLiveData()
    val agency: MutableLiveData<Agency> = MutableLiveData()

    val constatHeaderInfo = MutableLiveData<String>()
    fun getTenantById(objectId: String): LiveData<Tenant?> = tenantRepository.getById(objectId).asLiveData()
    fun getContractorById(objectId: String): LiveData<Contractor?>  = contractorRepository.getById(objectId).asLiveData()
    fun getOwnerById(objectId: String): LiveData<Owner?>  = ownerRepository.getById(objectId).asLiveData()
    fun getPropertyById(objectId: String): LiveData<Property?>  = propertyRepository.getById(objectId).asLiveData()
    fun getUserById(objectId: String): LiveData<Users?>  = userRepository.getById(objectId).asLiveData()
    fun getAgencyById(objectId: String): LiveData<Agency?>  = agencyRepository.getById(objectId).asLiveData()
}