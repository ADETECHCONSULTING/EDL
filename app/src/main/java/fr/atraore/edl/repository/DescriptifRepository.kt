package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.DescriptifDao
import fr.atraore.edl.data.models.Descriptif
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class DescriptifRepository @Inject constructor(
    private val dao: DescriptifDao
) : BaseRepository<Descriptif>(dao) {
    fun getAll(): Flow<List<Descriptif>> = dao.getAll()
    fun getById(id: String): Flow<Descriptif> = dao.getById(id)
}