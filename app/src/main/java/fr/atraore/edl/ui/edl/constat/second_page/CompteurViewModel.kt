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
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CompteurViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()

    val compteurEauFroid = Compteur(constatId, 1)
    val compteurElec = Compteur(constatId, 2)
    val compteurDetecFumee = Compteur(constatId, 3)
    val compteurEauChaude = Compteur(constatId, 4)
    val compteurGaz = Compteur(constatId, 5)
    val compteurCuve = Compteur(constatId, 6)

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): CompteurViewModel
    }

}