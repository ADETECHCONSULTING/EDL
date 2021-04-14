package fr.atraore.edl.ui.edl.search.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Users
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.UserRepository

class UserSearchViewModel(
    private val repository: UserRepository,
    private val constatRepository: ConstatRepository
): ViewModel() {
    val allUsers: LiveData<List<Users>> = repository.allUsers.asLiveData()

    suspend fun updateExistingUser(constat: Constat, user: Users) {
        constatRepository.updateExistingUserCrossRef(constat.constatId, user.userId)
    }

    suspend fun save(constat: Constat, user: Users) {
        constatRepository.saveConstatUserCrossRef(constat.constatId, user.userId)
    }
}

class UserSearchViewModelFactory(
    private val repository: UserRepository,
    private val constatRepository: ConstatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserSearchViewModel(repository, constatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}