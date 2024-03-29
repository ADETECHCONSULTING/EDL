package fr.atraore.edl.ui.edl.constat.signature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.repository.ConfigPdfRepository
import fr.atraore.edl.repository.ConstatRepository

class SignatureViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    configPdfRepository: ConfigPdfRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> =
        repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    val configPdf = configPdfRepository.getFirst().asLiveData()

    suspend fun saveOwnerSignaturePath(path: String, constatId: String) {
        repository.saveOwnerSignaturePath(path, constatId)
    }

    suspend fun saveTenantSignaturePath(path: String, constatId: String) {
        repository.saveTenantSignaturePath(path, constatId)
    }

    suspend fun saveParaphPath(path: String, constatId: String) {
        repository.saveParaphPath(path, constatId)
    }

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): SignatureViewModel
    }
}