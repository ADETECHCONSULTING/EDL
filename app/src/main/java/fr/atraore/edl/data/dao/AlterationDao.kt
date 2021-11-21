package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import fr.atraore.edl.data.models.Alteration
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.utils.ALTERATION_TABLE
import fr.atraore.edl.utils.DETAIL_TABLE
import fr.atraore.edl.utils.TENANT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface AlterationDao : BaseDao<Alteration> {
    //** GET **
    //Select all detail
    @Query("SELECT * FROM $ALTERATION_TABLE")
    fun getAll(): Flow<List<Alteration>>

    @Query("SELECT * FROM $ALTERATION_TABLE WHERE id =:id")
    fun getById(id: String): Flow<Alteration>

}