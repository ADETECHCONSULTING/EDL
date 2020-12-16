package fr.atraore.edl.data.dao

import androidx.room.Query
import fr.atraore.edl.data.models.Agency
import fr.atraore.edl.utils.AGENCY_TABLE
import kotlinx.coroutines.flow.Flow

interface AgencyDao : BaseDao<Agency> {
    //** GET **
    //Select all agency
    @Query("SELECT * FROM $AGENCY_TABLE")
    fun getAllAgencies(): Flow<List<Agency>>

}