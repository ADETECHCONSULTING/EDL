package fr.atraore.edl.data.dao

import androidx.room.Query
import fr.atraore.edl.data.models.Users
import fr.atraore.edl.utils.USERS_TABLE
import kotlinx.coroutines.flow.Flow

interface UserDao : BaseDao<Users> {
    //** GET **
    //Select all utilisateurs
    @Query("SELECT * FROM $USERS_TABLE")
    fun getAllUsers(): Flow<List<Users>>

}