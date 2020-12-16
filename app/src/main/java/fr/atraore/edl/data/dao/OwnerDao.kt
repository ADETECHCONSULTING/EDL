package fr.atraore.edl.data.dao

import androidx.room.Query
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.utils.OWNER_TABLE
import kotlinx.coroutines.flow.Flow

interface OwnerDao: BaseDao<Owner> {
    //** GET **
    //Select all propriétaire
    @Query("SELECT * FROM $OWNER_TABLE")
    fun getAllOwners(): Flow<List<Owner>>

}