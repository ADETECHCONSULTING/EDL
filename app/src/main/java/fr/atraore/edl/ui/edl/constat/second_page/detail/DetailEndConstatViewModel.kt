package fr.atraore.edl.ui.edl.constat.second_page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.repository.*
import fr.atraore.edl.utils.QuadrupleCombinedLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEndConstatViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    private val alterationRepository: AlterationRepository,
    private val descriptifRepository: DescriptifRepository,
): ViewModel() {
    fun getDetailById(id: String): LiveData<Detail> = detailRepository.getDetailById(id).asLiveData()

    val getAllAlterations = alterationRepository.getAll().asLiveData()
    val getAllDescriptifs = descriptifRepository.getAll().asLiveData()
    lateinit var currentDetail: Detail

    fun saveDetail(detail: Detail) {
        viewModelScope.launch {
            detailRepository.save(detail)
        }
    }

    suspend fun updateEtat(etat: String, idDetail: String) {
        detailRepository.updateEtat(etat, idDetail)
    }

    suspend fun updateProprete(proprete: String, idDetail: String) {
        detailRepository.updateProprete(proprete, idDetail)
    }

    suspend fun saveAlteration(alteration: Alteration) {
        alterationRepository.save(alteration)
    }

    suspend fun saveDescriptif(descriptif: Descriptif) {
        descriptifRepository.save(descriptif)
    }

    suspend fun updateFonctionnement(fonctionnement: String, idDetail: String) {
        detailRepository.updateFonctionnement(fonctionnement, idDetail)
    }

    suspend fun updateImagesPaths(imagesPaths: String, idDetail: String) {
        detailRepository.updateImagesPaths(imagesPaths, idDetail)
    }
}