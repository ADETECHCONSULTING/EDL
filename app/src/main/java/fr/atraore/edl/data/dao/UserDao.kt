package fr.atraore.edl.data.dao

import androidx.room.Dao
import androidx.room.Query
import fr.atraore.edl.data.models.entity.Users
import fr.atraore.edl.utils.USERS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<Users> {
    //** GET **
    //Select all utilisateurs
    @Query("SELECT * FROM $USERS_TABLE")
    fun getAllUsers(): Flow<List<Users>>

    @Query("SELECT * FROM $USERS_TABLE LIMIT 1")
    fun getLastUser(): Flow<Users>
}