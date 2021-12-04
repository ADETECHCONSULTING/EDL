package fr.atraore.edl.ui.edl.constat.second_page

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.data.RoomWithDetails
import fr.atraore.edl.repository.*
import fr.atraore.edl.utils.COMPTEUR_LABELS
import fr.atraore.edl.utils.CombinedLiveData
import fr.atraore.edl.utils.TripleCombinedLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CompteurViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val compteurRepository: CompteurRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()

    lateinit var compteurEauFroide: Compteur
    lateinit var compteurElec: Compteur
    lateinit var compteurDetecFumee: Compteur
    lateinit var compteurEauChaude: Compteur
    lateinit var compteurGaz: Compteur
    lateinit var compteurCuve: Compteur

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): CompteurViewModel
    }

    fun setCompteurs() {
        viewModelScope.launch {
            compteurRepository.getById(constatId, 1).collect {
                compteurEauFroide = it ?: Compteur(constatId, 1)
            }
            compteurRepository.getById(constatId, 2).collect {
                compteurElec = it ?: Compteur(constatId, 2)
            }
            compteurRepository.getById(constatId, 3).collect {
                compteurDetecFumee = it ?: Compteur(constatId, 3)
            }
            compteurRepository.getById(constatId, 4).collect {
                compteurEauChaude = it ?: Compteur(constatId, 4)
            }
            compteurRepository.getById(constatId, 5).collect {
                compteurGaz = it ?: Compteur(constatId, 5)
            }
            compteurRepository.getById(constatId, 6).collect {
                compteurCuve = it ?: Compteur(constatId, 6)
            }
        }
    }

}