package fr.atraore.edl.ui.edl

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.data.models.*
import fr.atraore.edl.repository.ContractorRepository
import fr.atraore.edl.repository.OwnerRepository
import fr.atraore.edl.repository.PropertyRepository
import fr.atraore.edl.repository.TenantRepository

abstract class BaseFragment<T> : Fragment() {
    abstract val title: String
    lateinit var tenantRepository: TenantRepository
    lateinit var propertyRepository: PropertyRepository
    lateinit var ownerRepository: OwnerRepository
    lateinit var contractorRepository: ContractorRepository
    /**
     * Sauvegarde dans le bon repository
     * https://stackoverflow.com/a/53589293
     */
    suspend fun <T: Any> save(obj: T) {

        when (obj::class) {
            Tenant::class -> {
                tenantRepository.save(obj as Tenant)
            }
            Property::class -> {
                val property: Property = obj as Property
                propertyRepository.save(property)
            }
            Owner::class -> {
                ownerRepository.save(obj as Owner)
            }
            Contractor::class -> {
                contractorRepository.save(obj as Contractor)
            }
            else -> println("Type not recognized")
        }
    }

}