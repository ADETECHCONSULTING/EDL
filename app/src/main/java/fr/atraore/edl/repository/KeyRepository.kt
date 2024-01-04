package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.KeyDao
import fr.atraore.edl.data.models.entity.KeyReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KeyRepository @Inject constructor(
    private val dao: KeyDao
) : BaseRepository<KeyReference>(dao) {
    fun getAll(): Flow<List<KeyReference>> = dao.getAll()
    fun getAllActifKeysRef(): Flow<List<KeyReference>> = dao.getAllActifKeysRef()
    fun getById(id: String): Flow<KeyReference> = dao.getById(id)
    fun searchKeysQuery(query: String): Flow<List<KeyReference>> = dao.searchKeysQuery(query)
}