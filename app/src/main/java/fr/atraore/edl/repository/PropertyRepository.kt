package fr.atraore.edl.repository

import androidx.annotation.WorkerThread
import fr.atraore.edl.data.dao.PropertyDao
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Property
import kotlinx.coroutines.flow.Flow

class PropertyRepository(
    private val propertyDao: PropertyDao
) {
    val allProperties: Flow<List<Property>> = propertyDao.getAllProperties()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun save(property: Property) {
        propertyDao.save(property)
    }
}