package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.PropertyDao
import fr.atraore.edl.data.models.entity.Property
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepository @Inject constructor(
    private val propertyDao: PropertyDao
) : BaseRepository<Property>(propertyDao) {

    val allProperties: Flow<List<Property>> = propertyDao.getAllProperties()
    fun getById(id: String): Flow<Property> = propertyDao.getById(id)

}