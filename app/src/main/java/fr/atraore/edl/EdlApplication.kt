package fr.atraore.edl

import android.app.Application
import fr.atraore.edl.data.AppDatabase
import fr.atraore.edl.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class EdlApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val constatRepository by lazy { ConstatRepository(database.getConstatDao()) }
    val propertyRepository by lazy { PropertyRepository(database.getPropertyDao()) }
    val ownerRepository by lazy { OwnerRepository(database.getOwnerDao()) }
    val agencyRepository by lazy { AgencyRepository(database.getAgencyDao()) }
    val contractorRepository by lazy { ContractorRepository(database.getContractorDao()) }
    val tenantRepository by lazy { TenantRepository(database.getTenantDao()) }
    val userRepository by lazy { UserRepository(database.getUserDao()) }
}