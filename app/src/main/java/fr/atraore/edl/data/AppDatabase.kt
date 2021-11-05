package fr.atraore.edl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.atraore.edl.data.dao.*
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

const val DATABASE_NAME = "edlDb"

@Database(
    entities = [
        Constat::class,
        ConstatOwnerCrossRef::class,
        ConstatPropertyCrossRef::class,
        ConstatTenantCrossRef::class,
        ConstatContractorCrossRef::class,
        ConstatAgencyCrossRef::class,
        ConstatUsersCrossRef::class,
        ConstatRoomCrossRef::class,
        Owner::class,
        Property::class,
        Tenant::class,
        Contractor::class,
        Agency::class,
        Users::class,
        RoomReference::class,
        ElementReference::class,
        LotReference::class
    ], version = 2, exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    //Daos
    abstract fun getConstatDao(): ConstatDao
    abstract fun getPropertyDao(): PropertyDao
    abstract fun getAgencyDao(): AgencyDao
    abstract fun getContractorDao(): ContractorDao
    abstract fun getTenantDao(): TenantDao
    abstract fun getUserDao(): UserDao
    abstract fun getOwnerDao(): OwnerDao
    abstract fun getRoomReferenceDao(): RoomReferenceDao
    abstract fun getElementReferenceDao(): ElementReferenceDao
    abstract fun getLotReferenceDao(): LotReferenceDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null
    }

}