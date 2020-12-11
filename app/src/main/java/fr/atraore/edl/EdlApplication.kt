package fr.atraore.edl

import android.app.Application
import fr.atraore.edl.data.AppDatabase
import fr.atraore.edl.repository.ConstatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class EdlApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ConstatRepository(database.getConstatDao()) }
}