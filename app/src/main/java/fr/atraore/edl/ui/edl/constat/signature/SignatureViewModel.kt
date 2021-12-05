package fr.atraore.edl.ui.edl.constat.signature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.repository.ConfigPdfRepository
import fr.atraore.edl.repository.ConstatRepository

class SignatureViewModel @AssistedInject constructor(
    val repository: ConstatRepository,
    val configPdfRepository: ConfigPdfRepository,
    @Assisted val constatId: String,
) : ViewModel() {
    val constatDetail: LiveData<ConstatWithDetails> =
        repository.getConstatDetail(constatId).asLiveData()
    val constatHeaderInfo = MutableLiveData<String>()
    val configPdf = configPdfRepository.getFirst().asLiveData()

    @AssistedFactory
    interface AssistedStartFactory {
        fun create(itemId: String): SignatureViewModel
    }
}