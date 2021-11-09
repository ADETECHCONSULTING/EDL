package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.AgencyDao
import fr.atraore.edl.data.dao.ElementReferenceDao
import fr.atraore.edl.data.dao.RoomReferenceDao
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.data.models.RoomReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElementRepository @Inject constructor(
    private val elementReferenceDao: ElementReferenceDao
) : BaseRepository<ElementReference>(elementReferenceDao) {

    fun allElementReference() : Flow<List<ElementReference>> = elementReferenceDao.getAllElementReference()
}