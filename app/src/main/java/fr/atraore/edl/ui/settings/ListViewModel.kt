package fr.atraore.edl.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.EquipmentReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.repository.EquipmentRepository
import fr.atraore.edl.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(val repository: EquipmentRepository, val roomRepository: RoomRepository) : ViewModel() {
    // Sample list data
    fun listDataFirstLevel(idRoomRef: Int) = repository.getFirstLevelItems(idRoomRef).asLiveData()
    fun listDataSecondLevel(idRoomRef: Int) = repository.getSecondLevelItems(idRoomRef).asLiveData()
    fun listDataThirdLevel(idRoomRef: Int) = repository.getThirdLevelItems(idRoomRef).asLiveData()

    fun filterLevelOneItems(query: String): LiveData<List<String>> = repository.filterLevelOneItems(query).asLiveData()
    fun filterLevelTwoItems(query: String): LiveData<List<String>> = repository.filterLevelTwoItems(query).asLiveData()
    fun filterLevelThreeItems(query: String): LiveData<List<String>> = repository.filterLevelThreeItems(query).asLiveData()

    fun getAllEquipmentReferences(): LiveData<List<EquipmentReference>> = repository.getAllEquipmentReferences().asLiveData()

    val getAllRoomReferences = roomRepository.getAllRoomReferences().asLiveData()

    fun saveEquipements(levelOne: String, levelTwo: String, levelThree: String, lotId: Int, idRoomRef: Int) {
        viewModelScope.launch {
            repository.save(EquipmentReference(UUID.randomUUID().toString(), levelOne, levelTwo, levelThree, idRoomRef, lotId))
        }
    }

    fun insertRoom(roomName: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                roomRepository.save(RoomReference(null, UUID.randomUUID().toString(), roomName))
                result.postValue(true)
            } catch (e: Exception) {
                result.postValue(false)
            }
        }
        return result
    }

    fun updateEquipmentReference(equipmentReference: EquipmentReference) {
        viewModelScope.launch {
            repository.save(equipmentReference)
        }
    }

    fun equipmentExists(levelOne: String, levelTwo: String, levelThree: String, lotId: Int, idRoomRef: Int) = repository.equipmentExists(levelOne, levelTwo, levelThree, lotId, idRoomRef).asLiveData()

    fun deleteEquipmentRef(id: String) {
        viewModelScope.launch {
            repository.deleteEquipmentRef(id)
        }
    }
}
