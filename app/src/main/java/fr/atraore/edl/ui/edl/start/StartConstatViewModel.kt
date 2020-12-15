package fr.atraore.edl.ui.edl.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.repository.ConstatRepository
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class StartConstatViewModel(
    val repository: ConstatRepository,
    val constatId: String
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
    val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

    //TODO Update methods
}

class StartConstatViewModelFactory(
    private val repository: ConstatRepository,
    private val constatId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartConstatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartConstatViewModel(repository, constatId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}