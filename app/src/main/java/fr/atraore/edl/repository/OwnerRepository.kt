package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.OwnerDao
import fr.atraore.edl.data.models.entity.Owner
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OwnerRepository @Inject constructor(
    private val ownerDao: OwnerDao
) : BaseRepository<Owner>(ownerDao) {
    val allOwners: Flow<List<Owner>> = ownerDao.getAllOwners()
    fun getById(id: String): Flow<Owner> = ownerDao.getById(id)
}