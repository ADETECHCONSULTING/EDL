package fr.atraore.edl.ui.edl.constat.second_page

import android.view.View
import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.entity.Compteur
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.repository.CompteurRepository
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.utils.COUPE_LABEL
import fr.atraore.edl.utils.EN_SERVICE_LABEL
import fr.atraore.edl.utils.EQUIPE_LABEL
import fr.atraore.edl.utils.NON_EQUIPE_LABEL
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CompteurViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val compteurRepository: CompteurRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()

    val compteurEauFroide: MutableLiveData<Compteur> = MutableLiveData()
    val compteurElec: MutableLiveData<Compteur> = MutableLiveData()
    val compteurDetecFumee: MutableLiveData<Compteur> = MutableLiveData()
    val compteurEauChaude: MutableLiveData<Compteur> = MutableLiveData()
    val compteurGaz: MutableLiveData<Compteur> = MutableLiveData()
    val compteurCuve: MutableLiveData<Compteur> = MutableLiveData()

    var visibilityEauChaude: MutableLiveData<Int> = MutableLiveData(View.GONE)
    var visibilityGaz: MutableLiveData<Int> = MutableLiveData(View.GONE)
    var visibilityCuve: MutableLiveData<Int> = MutableLiveData(View.GONE)


    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): CompteurViewModel
    }

    fun setCompteurs() {
        viewModelScope.launch {
            compteurRepository.getById(constatId, 1).collect {
                if (compteurEauFroide.value == null) {
                    compteurEauFroide.value = it ?: Compteur(constatId, 1)
                    if (it == null) {
                        compteurRepository.save(compteurEauFroide.value!!)
                    }
                }
            }
        }
        viewModelScope.launch {
            if (compteurElec.value == null) {
                compteurRepository.getById(constatId, 2).collect {
                    compteurElec.value = it ?: Compteur(constatId, 2)
                    if (it == null) {
                        compteurRepository.save(compteurElec.value!!)
                    }
                }
            }
        }
        viewModelScope.launch {
            if (compteurDetecFumee.value == null) {
                compteurRepository.getById(constatId, 3).collect {
                    compteurDetecFumee.value = it ?: Compteur(constatId, 3)
                    if (it == null) {
                        compteurRepository.save(compteurDetecFumee.value!!)
                    }
                }
            }
        }
        viewModelScope.launch {
            if (compteurEauChaude.value == null) {
                compteurRepository.getById(constatId, 4).collect {
                    //les compteurs optionnels on n'instancie pas directement mais que lorsque
                    //la visibilite passe à true
                    it?.let {
                        visibilityEauChaude.value = View.VISIBLE
                        compteurEauChaude.value = it
                    }
                }
            }
        }
        viewModelScope.launch {
            if (compteurGaz.value == null) {
                compteurRepository.getById(constatId, 5).collect {
                    it?.let {
                        visibilityGaz.value = View.VISIBLE
                        compteurGaz.value = it
                    }
                }
            }
        }
        viewModelScope.launch {
            if (compteurCuve.value == null) {
                compteurRepository.getById(constatId, 6).collect {
                    it?.let {
                        visibilityCuve.value = View.VISIBLE
                        compteurCuve.value = it
                    }
                }
            }
        }
    }

    fun saveCompteurs() {
        viewModelScope.launch {
            compteurRepository.saveArgsNullable(
                compteurEauFroide.value,
                compteurElec.value,
                compteurDetecFumee.value,
                compteurEauChaude.value,
                compteurGaz.value,
                compteurCuve.value
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


    fun getEnServiceState(compteurLabel: String): Boolean {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                return compteurEauFroide.value?.etat == EN_SERVICE_LABEL
            }
            "Compteur d'électricité" -> {
                return compteurElec.value?.etat == EN_SERVICE_LABEL
            }
            "Détecteur de fumée" -> {
                return compteurDetecFumee.value?.etat == EQUIPE_LABEL
            }
            "Compteur d'eau chaude" -> {
                return compteurEauChaude.value?.etat == EN_SERVICE_LABEL
            }
            "Compteur Gaz" -> {
                return compteurGaz.value?.etat == EN_SERVICE_LABEL
            }
            "Cuve à fuel / gaz" -> {
                return compteurCuve.value?.etat == EN_SERVICE_LABEL
            }
        }
        return false
    }

    fun getCoupeState(compteurLabel: String): Boolean {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                return compteurEauFroide.value?.etat == COUPE_LABEL
            }
            "Compteur d'électricité" -> {
                return compteurElec.value?.etat == COUPE_LABEL
            }
            "Détecteur de fumée" -> {
                return compteurDetecFumee.value?.etat == NON_EQUIPE_LABEL
            }
            "Compteur d'eau chaude" -> {
                return compteurEauChaude.value?.etat == COUPE_LABEL
            }
            "Compteur Gaz" -> {
                return compteurGaz.value?.etat == COUPE_LABEL
            }
            "Cuve à fuel / gaz" -> {
                return compteurCuve.value?.etat == COUPE_LABEL
            }
        }
        return false
    }

    fun setEtatCompteur(compteurLabel: String, etat: String) {
        when (compteurLabel) {
            "Compteur d'eau froide" -> {
                compteurEauFroide.value?.etat = etat
                compteurEauFroide.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur d'électricité" -> {
                compteurElec.value?.etat = etat
                compteurElec.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Détecteur de fumée" -> {
                compteurDetecFumee.value?.etat = etat
                compteurDetecFumee.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur d'eau chaude" -> {
                compteurEauChaude.value?.etat = etat
                compteurEauChaude.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Compteur Gaz" -> {
                compteurGaz.value?.etat = etat
                compteurGaz.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
            "Cuve à fuel / gaz" -> {
                compteurCuve.value?.etat = etat
                compteurCuve.value?.let { viewModelScope.launch {compteurRepository.save(it) }}
            }
        }
    }
}