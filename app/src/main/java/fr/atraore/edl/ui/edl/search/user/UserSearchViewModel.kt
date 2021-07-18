package fr.atraore.edl.ui.edl.search.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.data.models.Users
import fr.atraore.edl.repository.ConstatRepository
import fr.atraore.edl.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
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