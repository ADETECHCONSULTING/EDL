package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.EtatDao
import fr.atraore.edl.data.models.Etat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class EtatRepository @Inject constructor(
    private val dao: EtatDao
) : BaseRepository<Etat>(dao) {
    fun getAll(): Flow<List<Etat>> = dao.getAll()
    fun getById(id: String): Flow<Etat> = dao.getById(id)
}