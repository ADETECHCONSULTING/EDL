package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.ConfigPdfDao
import fr.atraore.edl.data.models.ConfigPdf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigPdfRepository @Inject constructor(
    private val dao: ConfigPdfDao
) : BaseRepository<ConfigPdf>(dao) {
    fun getFirst(): Flow<ConfigPdf> = dao.getFirst()
    fun getById(id: String): Flow<ConfigPdf> = dao.getById(id)
}