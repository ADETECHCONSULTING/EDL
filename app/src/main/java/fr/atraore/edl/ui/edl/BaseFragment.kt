package fr.atraore.edl.ui.edl

import androidx.fragment.app.Fragment
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.data.models.*

abstract class BaseFragment<T> : Fragment() {
    abstract val title: String

    /**
     * Sauvegarde dans le bon repository
     * https://stackoverflow.com/a/53589293
     */
    suspend fun <T: Any> save(obj: T) {
        val edlApplication = activity?.application as EdlApplication

        when (obj::class) {
            Tenant::class -> {
                edlApplication.tenantRepository.save(obj as Tenant)
            }
            Property::class -> {
                val property: Property = obj as Property
                edlApplication.propertyRepository.save(property)
            }
            Owner::class -> {
                edlApplication.ownerRepository.save(obj as Owner)
            }
            Contractor::class -> {
                edlApplication.contractorRepository.save(obj as Contractor)
            }
            else -> println("Type not recognized")
        }
    }

}