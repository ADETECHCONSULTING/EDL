package fr.atraore.edl.ui

import androidx.lifecycle.*
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.repository.ConstatRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(
    private val constatRepositoy: ConstatRepository
) : ViewModel() {
    val allConstats: LiveData<List<Constat>> = constatRepositoy.allConstats.asLiveData()

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