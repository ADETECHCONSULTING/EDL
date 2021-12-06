package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.utils.OWNER_TABLE
import fr.atraore.edl.utils.PROPERTY_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao : BaseDao<Property> {
    //** GET **
    //Select all property from Table Property
    @Query("SELECT * FROM $PROPERTY_TABLE")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM $PROPERTY_TABLE WHERE propertyId = :id")
    fun getById(id: String): Flow<Property>

    @Update
    suspend fun saveProperties(properties: List<Property>)

}