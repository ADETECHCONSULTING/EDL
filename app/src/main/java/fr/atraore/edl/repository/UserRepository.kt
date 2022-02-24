package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.UserDao
import fr.atraore.edl.data.models.entity.Users
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) : BaseRepository<Users>(userDao) {
    val allUsers: Flow<List<Users>> = userDao.getAllUsers()
}