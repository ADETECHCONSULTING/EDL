package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ElementReferenceDao
import fr.atraore.edl.data.models.entity.ElementReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElementRepository @Inject constructor(
    private val elementReferenceDao: ElementReferenceDao
) : BaseRepository<ElementReference>(elementReferenceDao) {

    fun allElementReference() : Flow<List<ElementReference>> = elementReferenceDao.getAllElementReference()
}