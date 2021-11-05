package fr.atraore.edl.ui.edl

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.*
import fr.atraore.edl.repository.ContractorRepository
import fr.atraore.edl.repository.OwnerRepository
import fr.atraore.edl.repository.PropertyRepository
import fr.atraore.edl.repository.TenantRepository
import fr.atraore.edl.ui.MainViewModel
import fr.atraore.edl.ui.edl.add.property.AddPropertyViewModel
import fr.atraore.edl.ui.edl.first_page.StartConstatViewModel
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.assistedViewModel
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KClass

@AndroidEntryPoint
abstract class BaseFragment(val classType: String) : Fragment() {

    abstract val title: String

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_next)?.isVisible = true
        menu.findItem(R.id.action_previous)?.isVisible = true
    }

    /**
     * Sauvegarde dans le bon repository
     * https://stackoverflow.com/a/53589293
     */
    suspend fun <T: Any> save(obj: T) {

        when (obj::class) {
            Tenant::class -> {
                mainViewModel.tenantRepository.save(obj as Tenant)
            }
            Property::class -> {
                val property: Property = obj as Property
                mainViewModel.propertyRepository.save(property)
            }
            Owner::class -> {
                mainViewModel.ownerRepository.save(obj as Owner)
            }
            Contractor::class -> {
                mainViewModel.contractorRepository.save(obj as Contractor)
            }
            else -> println("Type not recognized")
        }
    }

    abstract fun goNext()

    fun goBack() {
        findNavController().popBackStack()
    }

}