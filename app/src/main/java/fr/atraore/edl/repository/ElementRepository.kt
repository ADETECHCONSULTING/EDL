package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ElementReferenceDao
import fr.atraore.edl.data.models.entity.ElementReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElementRepository @Inject constructor(
    private val elementReferenceDao: ElementReferenceDao
) : BaseRepository<ElementReference>(elementReferenceDao) {

    fun searchElementQuery(query: String): Flow<List<ElementReference>> = elementReferenceDao.searchElementQuery(query)
    fun getElementsRefWhereLotId(idLot: Int): Flow<List<ElementReference>> = elementReferenceDao.getElementsRefWhereLotId(idLot)
    fun getElementsForRoom(idLot: Int): Flow<List<ElementReference>> = elementReferenceDao.getElementsForRoom(idLot)
    fun getElementsRefChild(elementReferenceId: String): Flow<List<ElementReference>> = elementReferenceDao.getElementsRefChild(elementReferenceId)
    
}