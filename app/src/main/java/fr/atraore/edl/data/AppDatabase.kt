package fr.atraore.edl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.atraore.edl.data.dao.*
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.crossRef.*
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
        ConstatAgencyCrossRef::class,
        ConstatUsersCrossRef::class,
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
    abstract fun getAgencyDao(): AgencyDao
    abstract fun getContractorDao(): ContractorDao
    abstract fun getTenantDao(): TenantDao
    abstract fun getUserDao(): UserDao
    abstract fun getOwnerDao(): OwnerDao

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
                    populateDatabase(
                        database.getConstatDao(),
                        database.getPropertyDao(),
                        database.getAgencyDao(),
                        database.getTenantDao(),
                        database.getOwnerDao(),
                        database.getUserDao(),
                        database.getContractorDao()
                    )
                }
            }
        }

        //Init with data
        suspend fun populateDatabase(
            //TODO remove after formulaire

            constatDao: ConstatDao,
            propertyDao: PropertyDao,
            agencyDao: AgencyDao,
            tenantDao: TenantDao,
            ownerDao: OwnerDao,
            userDao: UserDao,
            contractorDao: ContractorDao
        ) {
            // Delete all content
            constatDao.deleteAll()


            //Add Sample Constat
            var constat = Constat(
                constatId = UUID.randomUUID().toString(),
                typeConstat = "E",
                dateCreation = Date(1607686070062),
                state = 0
            )
            constatDao.save(constat)

            /*
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

            val property2 = Property(
                propertyId = UUID.randomUUID().toString(),
                address = "38 rue des Bellevue",
                address2 = "",
                postalCode = "69008",
                "Landivisiau",
                "",
                "Maison",
                "F5",
                2,
                "Avec Jardin",
                1,
                1,
                1,
                1,
                1,
                1,
                0
            )

            val property3 = Property(
                propertyId = UUID.randomUUID().toString(),
                address = "50 avenue debourg",
                address2 = "",
                postalCode = "69007",
                "Lyon",
                "",
                "Appartement",
                "F2",
                1,
                "",
                1,
                1,
                15,
                1,
                1,
                1,
                0
            )

            val property4 = Property(
                propertyId = UUID.randomUUID().toString(),
                address = "50 avenue debourg",
                address2 = "",
                postalCode = "69007",
                "Lyon",
                "",
                "Appartement",
                "F2",
                1,
                "",
                1,
                1,
                15,
                1,
                1,
                1,
                0
            )
            propertyDao.save(property, property2, property3, property4)

            val agency = Agency(
                UUID.randomUUID().toString(),
                "Adam'Service",
                "155 boulevard des états-unis 69008",
                "",
                "",
                "",
                "0627562283",
                "",
                "",
                1,
                ""
            )
            agencyDao.save(agency)

            val user = Users(
                UUID.randomUUID().toString(),
                "Monsieur",
                "Adama",
                "Test",
                "Test",
                "Test",
                "Test",
                "Test",
                "Test",
                "Test",
                "Test",
                1
            )
            userDao.save(user)

            val proprietaire = Owner(
                UUID.randomUUID().toString(),
                "M",
                "Adama",
                "155 boulevard des états-unis",
                "",
                "69008",
                "Lyon",
                "0627562283",
                "",
                "a.traore.pro@gmail.com",
                ""
            )
            ownerDao.save(proprietaire)

            val contractor = Contractor(
                UUID.randomUUID().toString(),
                "Guy Hoquet",
                "a.traore.pro@gmail.com",
                "18 rue Léandre vaillat",
                "",
                "",
                "74100",
                "",
                "",
                "Annemasse"
            )
            contractorDao.save(contractor)

            val tenant = Tenant(
                UUID.randomUUID().toString(),
                "Mme",
                "Amiour Caroline",
                "745 rue de bonneville",
                "",
                "74300",
                "Annemasse",
                "0620577356",
                "",
                "caroamiour@hotmail.fr",
                "",
                "",
                "",
                "",
                "",
                Date(1607686070062)
            )
            val tenant2 = Tenant(
                UUID.randomUUID().toString(),
                "M",
                "Gilbert Louvin",
                "525 rue de beauvisage",
                "",
                "69008",
                "Lyon",
                "0620577356",
                "",
                "giblouv@hotmail.fr",
                "",
                "",
                "",
                "",
                "",
                Date(1607686070062)
            )
            tenantDao.save(tenant)
            tenantDao.save(tenant2)


            val constatWithProperty = ConstatPropertyCrossRef(
                constat.constatId,
                property.propertyId
            )
            val constatWithOwner = ConstatOwnerCrossRef(
                constat.constatId,
                proprietaire.ownerId
            )
            val constatWithTenant = ConstatTenantCrossRef(
                constat.constatId,
                tenant.tenantId
            )
            val constatWithAgency = ConstatAgencyCrossRef(
                constat.constatId,
                agency.agencyId
            )
            val constatWithUser = ConstatUsersCrossRef(
                constat.constatId,
                user.userId
            )
            constatDao.saveConstatPropertyCrossRef(constatWithProperty)
            constatDao.saveConstatOwnerCrossRef(constatWithOwner)
            constatDao.saveConstatTenantCrossRef(constatWithTenant)
            constatDao.saveConstatAgencyCrossRef(constatWithAgency)
            constatDao.saveConstatUsersCrossRef(constatWithUser)
        }
    }

}