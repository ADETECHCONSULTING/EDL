package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.OwnerDao
import fr.atraore.edl.data.models.Owner
import kotlinx.coroutines.flow.Flow

class OwnerRepository(
    private val ownerDao: OwnerDao
) : BaseRepository<Owner>(ownerDao) {

    val allOwners: Flow<List<Owner>> = ownerDao.getAllOwners()

}