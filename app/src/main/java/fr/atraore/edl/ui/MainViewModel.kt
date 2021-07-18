package fr.atraore.edl.ui

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.repository.ConstatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val constatRepositoy: ConstatRepository
) : ViewModel() {
    val allConstats: LiveData<List<Constat>> = constatRepositoy.allConstats.asLiveData()
    val allConstatWithDetails: LiveData<List<ConstatWithDetails>> = constatRepositoy.allConstatWithDetails.asLiveData()
    val corountineContext: CoroutineContext
    get() = Dispatchers.IO

    fun saveConstat(constat: Constat) = viewModelScope.launch {
        constatRepositoy.save(constat)
    }
}

class MainViewModelFactory(
    private val repository: ConstatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}