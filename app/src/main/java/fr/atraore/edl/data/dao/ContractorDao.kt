package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.utils.CONTRACTOR_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractorDao : BaseDao<Contractor> {
    //** GET **
    //Select all contractors
    @Query("SELECT * FROM $CONTRACTOR_TABLE")
    fun getAllContractors(): Flow<List<Contractor>>

}