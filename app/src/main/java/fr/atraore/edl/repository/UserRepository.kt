package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.UserDao
import fr.atraore.edl.data.models.Users
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) : BaseRepository<Users>(userDao) {
    val allUsers: Flow<List<Users>> = userDao.getAllUsers()
}