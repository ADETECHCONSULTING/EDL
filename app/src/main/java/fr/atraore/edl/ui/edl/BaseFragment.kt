package fr.atraore.edl.ui.edl

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.ui.MainViewModel

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
        menu.findItem(R.id.action_add_agency)?.isVisible = false
        menu.findItem(R.id.action_add_user)?.isVisible = false
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
            Users::class -> {
                mainViewModel.userRepository.save(obj as Users)
            }
            Agency::class -> {
                mainViewModel.agencyRepository.save(obj as Agency)
            }
            else -> println("Type not recognized")
        }
    }

    abstract fun goNext()

    fun goBack() {
        findNavController().popBackStack()
    }


    fun getConstatEtat(etat: String): String {
        when (etat) {
            "E" -> {
                return "entrant"
            }
            "PE" -> {
                return "pré-état"
            }
            "S" -> {
                return "sortant"
            }
            else -> {
                return ""
            }
        }
    }

}