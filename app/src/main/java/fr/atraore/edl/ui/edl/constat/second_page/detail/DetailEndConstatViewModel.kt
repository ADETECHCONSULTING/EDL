package fr.atraore.edl.ui.edl.constat.second_page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.repository.DetailRepository
import javax.inject.Inject

@HiltViewModel
class DetailEndConstatViewModel @Inject constructor(
    private val detailRepository: DetailRepository
): ViewModel() {
    fun getDetailById(id: String): LiveData<Detail> = detailRepository.getDetailById(id).asLiveData()

    suspend fun save(detail: Detail) {
        detailRepository.save()
    }
}