package fr.atraore.edl.ui.edl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.atraore.edl.repository.BaseRepository
import kotlinx.coroutines.launch

//generic view model
class BaseViewModel<T>(
    val baseRepository: BaseRepository<T>
) : ViewModel() {

    fun save(obj: T) = viewModelScope.launch {
        baseRepository.save(obj)
    }

}

class BaseViewModelFactory<T>(
    val baseRepository: BaseRepository<T>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BaseViewModel(baseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}