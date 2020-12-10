package fr.atraore.edl.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.atraore.edl.data.models.CONSTAT_TABLE
import fr.atraore.edl.data.models.Constat

@Dao
interface ConstatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(constat: Constat)

    @Update
    fun update(constat: Constat)

    //Select all constat from Table Constat
    @Query("SELECT * FROM $CONSTAT_TABLE")
    fun getAllConstat(): LiveData<Constat>

    //Select constat by id
    @Query("SELECT * FROM $CONSTAT_TABLE WHERE id = :constatId")
    fun getConstatById(constatId: Int)

    @Delete
    fun delete(constat: Constat)
}