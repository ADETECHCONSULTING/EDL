package fr.atraore.edl.ui.edl.constat.second_page

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import androidx.room.Ignore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.data.RoomWithDetails
import fr.atraore.edl.databinding.CompteurFragmentBinding
import fr.atraore.edl.repository.*
import fr.atraore.edl.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CompteurViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val compteurRepository: CompteurRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> =
        repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()

    var compteurEauFroide: Compteur? = null
    var compteurElec: Compteur? = null
    var compteurDetecFumee: Compteur? = null
    var compteurEauChaude: Compteur? = null
    var compteurGaz: Compteur? = null
    var compteurCuve: Compteur? = null

    var visibilityEauChaude: Int = View.GONE
    var visibilityGaz: Int = View.GONE
    var visibilityCuve: Int = View.GONE


    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): CompteurViewModel
    }

    fun setCompteurs(binding: CompteurFragmentBinding) {
        viewModelScope.launch {
            compteurRepository.getById(constatId, 1).collect {
                compteurEauFroide = it ?: Compteur(constatId, 1)
                if (it == null) {
                    compteurRepository.save(compteurEauFroide!!)
                }
                binding.invalidateAll()
            }
        }
        viewModelScope.launch {
            compteurRepository.getById(constatId, 2).collect {
                compteurElec = it ?: Compteur(constatId, 2)
                if (it == null) {
                    compteurRepository.save(compteurElec!!)
                }
            }
        }
        viewModelScope.launch {
            compteurRepository.getById(constatId, 3).collect {
                compteurDetecFumee = it ?: Compteur(constatId, 3)
                if (it == null) {
                    compteurRepository.save(compteurDetecFumee!!)
                }
            }
        }
        viewModelScope.launch {
            compteurRepository.getById(constatId, 4).collect {
                //les compteurs optionnels on n'instancie pas directement mais que lorsque
                //la visibilite passe à true
                it?.let {
                    compteurEauChaude = it
                }
            }
        }
        viewModelScope.launch {
            compteurRepository.getById(constatId, 5).collect {
                it?.let {
                    compteurGaz = it
                }
            }
        }
        viewModelScope.launch {
            compteurRepository.getById(constatId, 6).collect {
                it?.let {
                    compteurCuve = it
                }
            }
        }
    }

    fun saveCompteurs() {
        viewModelScope.launch {
            compteurRepository.saveArgsNullable(
                compteurEauFroide,
                compteurElec,
                compteurDetecFumee,
                compteurEauChaude,
                compteurGaz,
                compteurCuve
            )
        }
    }

    fun saveImagePathOnCompteur(compteurRefId: Int, imagePath: String, isSecond: Boolean) {
        viewModelScope.launch {
            compteurRepository.saveImagePath(compteurRefId, imagePath, isSecond)
        }
    }

    fun saveCompteur(compteurRefId: Int) {
        viewModelScope.launch {
            compteurRepository.save(Compteur(constatId, compteurRefId))
        }
    }

    var getCompteurEauChaudeVisibility: Int = View.GONE
        set(value) {
            field = value
            visibilityEauChaude = value
        }

    var getCompteurGazVisibility: Int = View.GONE
        set(value) {
            field = value
            visibilityGaz = value
        }

    var getCompteurCuveVisibility: Int = View.GONE
        set(value) {
            field = value
            visibilityCuve = value
        }


    fun getEnServiceState(compteurLabel: String): Boolean {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                return compteurEauFroide?.etat == EN_SERVICE_LABEL
            }
            "Compteur d'électricité" -> {
                return compteurElec?.etat == EN_SERVICE_LABEL
            }
            "Détecteur de fumée" -> {
                return compteurDetecFumee?.etat == EQUIPE_LABEL
            }
            "Compteur d'eau chaude" -> {
                return compteurEauChaude?.etat == EN_SERVICE_LABEL
            }
            "Compteur Gaz" -> {
                return compteurGaz?.etat == EN_SERVICE_LABEL
            }
            "Cuve à fuel / gaz" -> {
                return compteurCuve?.etat == EN_SERVICE_LABEL
            }
        }
        return false
    }

    fun getCoupeState(compteurLabel: String): Boolean {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                return compteurEauFroide?.etat == COUPE_LABEL
            }
            "Compteur d'électricité" -> {
                return compteurElec?.etat == COUPE_LABEL
            }
            "Détecteur de fumée" -> {
                return compteurDetecFumee?.etat == NON_EQUIPE_LABEL
            }
            "Compteur d'eau chaude" -> {
                return compteurEauChaude?.etat == COUPE_LABEL
            }
            "Compteur Gaz" -> {
                return compteurGaz?.etat == COUPE_LABEL
            }
            "Cuve à fuel / gaz" -> {
                return compteurCuve?.etat == COUPE_LABEL
            }
        }
        return false
    }

    fun setEtatCompteur(compteurLabel: String, etat: String) {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                compteurEauFroide?.etat = etat
                compteurEauFroide?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur d'électricité" -> {
                compteurElec?.etat = etat
                compteurElec?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Détecteur de fumée" -> {
                compteurDetecFumee?.etat = etat
                compteurDetecFumee?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur d'eau chaude" -> {
                compteurEauChaude?.etat = etat
                compteurEauChaude?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur Gaz" -> {
                compteurGaz?.etat = etat
                compteurGaz?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Cuve à fuel / gaz" -> {
                compteurCuve?.etat = etat
                compteurEauFroide?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
        }
    }
}