package fr.atraore.edl.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.repository.EquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(val repository: EquipmentRepository) : ViewModel() {
    // Sample list data
    fun listDataFirstLevel(idRoomRef: Int) = repository.getFirstLevelItems(idRoomRef).asLiveData()
    fun listDataSecondLevel(idRoomRef: Int) = repository.getSecondLevelItems(idRoomRef).asLiveData()
    fun listDataThirdLevel(idRoomRef: Int) = repository.getThirdLevelItems(idRoomRef).asLiveData()

    fun filterLevelOneItems(query: String): LiveData<List<String>> = repository.filterLevelOneItems(query).asLiveData()
    fun filterLevelTwoItems(query: String): LiveData<List<String>> = repository.filterLevelTwoItems(query).asLiveData()
    fun filterLevelThreeItems(query: String): LiveData<List<String>> = repository.filterLevelThreeItems(query).asLiveData()

    fun addItemFirstLevel(item: String, idRoomRef: Int) {
        viewModelScope.launch {
            repository.addItemFirstLevel(item, idRoomRef)
        }
    }

    fun addItemSecondLevel(level1: String, secondItem: String, idRoomRef: Int) {
        viewModelScope.launch {
            repository.updateItemSecondLevel(level1, secondItem, idRoomRef)
        }
    }

    fun addItemThirdLevel(level1: String, thirdItem: String, idRoomRef: Int) {
        viewModelScope.launch {
            repository.updateItemThirdLevel(level1, thirdItem, idRoomRef)
        }
    }

    fun equipmentExists(levelOne: String, levelTwo: String, levelThree: String, lotId: Int) = repository.equipmentExists(levelOne, levelTwo, levelThree, lotId).asLiveData()
}
