package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.AlterationDao
import fr.atraore.edl.data.models.Alteration
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class AlterationRepository @Inject constructor(
    private val dao: AlterationDao
) : BaseRepository<Alteration>(dao) {
    fun getAll(): Flow<List<Alteration>> = dao.getAll()
    fun getById(id: String): Flow<Alteration> = dao.getById(id)
}