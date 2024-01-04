package fr.atraore.edl.ui.pdf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.DetailRepository
import fr.atraore.edl.utils.CombinedLiveData
import javax.inject.Inject

@HiltViewModel
class PdfConstatCreatorViewModel @Inject constructor(
    private val repository: ConstatRepository,
    private val detailRepository: DetailRepository
) : ViewModel() {
    fun constatAndRoomDetailsCombined(constatId: String) = CombinedLiveData(
        repository.getConstatDetail(constatId).asLiveData(),
        detailRepository.getRoomDetails(constatId).asLiveData()
    )
}