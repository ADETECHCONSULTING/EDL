package fr.atraore.edl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.KeyReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.repository.ElementRepository
import fr.atraore.edl.repository.KeyRepository
import fr.atraore.edl.repository.RoomRepository
import javax.inject.Inject

@HiltViewModel
class ReferenceViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val elementRepository: ElementRepository,
    private val keyRepository: KeyRepository,
) : ViewModel() {
    val getElements = elementRepository.allElementReference().asLiveData()
    val getRooms = roomRepository.allRoomReferences().asLiveData()
    val getKeys = keyRepository.getAll().asLiveData()

    fun searchElementQuery(query: String) = elementRepository.searchElementQuery(query).asLiveData()
    fun getRoomWithElements(name: String, idLot: Int) = roomRepository.getRoomWithElements(name, idLot).asLiveData()
    fun getRoomWithNameAndIdLot(name: String, idLot: Int) = roomRepository.getRoomWithNameAndIdLot(name, idLot).asLiveData()
    fun searchKeysQuery(query: String) = keyRepository.searchKeysQuery(query).asLiveData()

    suspend fun saveRoomWithElements(idRoom: String, vararg idsElements: String) {
        idsElements.forEach { idElement ->
            roomRepository.saveRoomElementCrossRef(idRoom, idElement)
        }
    }

    suspend fun deleteRoomWithElements(idRoom: String, vararg idsElements: String) {
        idsElements.forEach { idElement ->
            roomRepository.deleteRoomElementCrossRef(idRoom, idElement)
        }
    }


    suspend fun saveRoom(roomReference: RoomReference) {
        roomRepository.save(roomReference)
    }

    suspend fun saveKey(keyReference: KeyReference) {
        keyRepository.save(keyReference)
    }
}