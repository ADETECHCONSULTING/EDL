package fr.atraore.edl.ui.edl.constat.second_page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.*
import fr.atraore.edl.repository.*
import fr.atraore.edl.utils.QuadrupleCombinedLiveData
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailEndConstatViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    private val alterationRepository: AlterationRepository,
    private val descriptifRepository: DescriptifRepository,
    private val etatRepository: EtatRepository,
    private val propreteRepository: PropreteRepository
): ViewModel() {
    fun getDetailById(id: String): LiveData<Detail> = detailRepository.getDetailById(id).asLiveData()
    val getAllAlterations = alterationRepository.getAll().asLiveData()
    val getAllEtats = etatRepository.getAll().asLiveData()
    val getAllDescriptifs = descriptifRepository.getAll().asLiveData()
    val getAllPropretes = propreteRepository.getAll().asLiveData()
    fun getDetailReferentiel() = QuadrupleCombinedLiveData(getAllAlterations, getAllDescriptifs, getAllEtats, getAllPropretes)

    suspend fun saveDetail(detail: Detail) {
        detailRepository.save(detail)
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

    suspend fun saveEtat(etat: Etat) {
        etatRepository.save(etat)
    }

    suspend fun saveProprete(proprete: Proprete) {
        propreteRepository.save(proprete)
    }
}