package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.CompteurDao
import fr.atraore.edl.data.models.Compteur
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.util.*

class CompteurRepository @Inject constructor(
    private val dao: CompteurDao
) : BaseRepository<Compteur>(dao) {
    fun getAll(): Flow<List<Compteur>> = dao.getAll()
    fun getById(idConstat: String, compteurRefId: Int): Flow<Compteur?> = dao.getById(idConstat, compteurRefId)
}