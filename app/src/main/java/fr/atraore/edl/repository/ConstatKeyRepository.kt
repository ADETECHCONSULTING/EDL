package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ConstatKeyDao
import fr.atraore.edl.data.models.entity.ConstatKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class ConstatKeyRepository @Inject constructor(
    private val dao: ConstatKeyDao
) : BaseRepository<ConstatKey>(dao) {
    fun getAll(): Flow<List<ConstatKey>> = dao.getAll()
    fun getAllFromConstat(constatId: String): Flow<List<ConstatKey>> = dao.getAllFromConstat(constatId)
    fun getById(id: String): Flow<ConstatKey> = dao.getById(id)
}