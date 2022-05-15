package fr.atraore.edl.ui.pdf

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.repository.ConstatRepository
import javax.inject.Inject

@HiltViewModel
class PdfConstatCreatorViewModel @Inject constructor(
    private val repository: ConstatRepository
) : ViewModel() {
    fun constatDetail(constatId: String): LiveData<ConstatWithDetails> = repository.getConstatDetail(constatId).asLiveData()
}