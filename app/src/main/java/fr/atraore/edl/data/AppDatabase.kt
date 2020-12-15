package fr.atraore.edl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.atraore.edl.data.dao.ConstatDao
import fr.atraore.edl.data.dao.PropertyDao
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.crossRef.ConstatContractorCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatOwnerCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatPropertyCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef
import fr.atraore.edl.utils.DateTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*

const val DATABASE_NAME = "edlDb"

@Database(
    entities = [
        Constat::class,
        ConstatOwnerCrossRef::class,
        ConstatPropertyCrossRef::class,
        ConstatTenantCrossRef::class,
        ConstatContractorCrossRef::class,
        Owner::class,
        Property::class,
        Tenant::class,
        Contractor::class,
        Agency::class,
        Users::class
    ], version = 1, exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getConstatDao(): ConstatDao
    abstract fun getPropertyDao(): PropertyDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.getConstatDao(), database.getPropertyDao())
                }
            }
        }

        //Init with data
        suspend fun populateDatabase(constatDao: ConstatDao, propertyDao: PropertyDao) {
/*            // Delete all content
            constatDao.deleteAll()

            //Add Sample Constat
            var constat = Constat(
                id = 0,
                typeConstat = "E",
                dateCreation = Date(1607686070062),
                idAgency = 1,
                idContractor = 1,
                idOwner = 1,
                idProperty = 1,
                idTenant = 1,
                idUser = 1,
                state = 0
            )
            var constat2 = Constat(
                id = 1,
                typeConstat = "S",
                dateCreation = Date(1607686070011),
                idAgency = 1,
                idContractor = 1,
                idOwner = 1,
                idProperty = 1,
                idTenant = 1,
                idUser = 1,
                state = 0
            )
            constatDao.save(constat)
            constatDao.save(constat2)
            */

            val property = Property(
                propertyId = UUID.randomUUID().toString(),
                address = "43 rue des couronnes",
                address2 = "",
                postalCode = "75020",
                "Paris",
                "",
                "Appartement",
                "F3",
                2,
                "",
                5,
                1,
                13,
                6,
                2,
                3,
                0
            )

            propertyDao.save(property)
        }
    }

}