package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.PropreteDao
import fr.atraore.edl.data.models.entity.Proprete
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropreteRepository @Inject constructor(
    private val dao: PropreteDao
) : BaseRepository<Proprete>(dao) {
    fun getAll(): Flow<List<Proprete>> = dao.getAll()
    fun getById(id: String): Flow<Proprete> = dao.getById(id)
}