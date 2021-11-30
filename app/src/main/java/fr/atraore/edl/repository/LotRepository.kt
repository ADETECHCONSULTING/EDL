package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.LotReferenceDao
import fr.atraore.edl.data.models.LotReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class LotRepository @Inject constructor(
    private val dao: LotReferenceDao
) : BaseRepository<LotReference>(dao) {
    fun getAll(): Flow<List<LotReference>> = dao.getAllLotReference()
    fun getAllMandatoryLotReference(): Flow<List<LotReference>> = dao.getAllMandatoryLotReference()
}