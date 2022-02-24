package fr.atraore.edl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.repository.ElementRepository
import fr.atraore.edl.repository.RoomRepository
import javax.inject.Inject

@HiltViewModel
class ReferenceViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val elementRepository: ElementRepository
) : ViewModel() {
    val getElements = elementRepository.allElementReference().asLiveData()
    val getRooms = roomRepository.allRoomReferences().asLiveData()
}