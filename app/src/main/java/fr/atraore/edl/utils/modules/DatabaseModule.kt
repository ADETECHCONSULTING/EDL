package fr.atraore.edl.utils.modules

import android.content.Context
import android.sax.Element
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.atraore.edl.data.AppDatabase
import fr.atraore.edl.data.DATABASE_NAME
import fr.atraore.edl.data.dao.*
import fr.atraore.edl.data.models.*
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.utils.ELEMENTS_LABELS
import fr.atraore.edl.utils.LOTS_LABELS
import fr.atraore.edl.utils.ROOMS_LABELS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideConstatDao(appDatabase: AppDatabase): ConstatDao {
        return appDatabase.getConstatDao()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.getUserDao()
    }

    @Provides
    fun provideAgencyDao(appDatabase: AppDatabase): AgencyDao {
        return appDatabase.getAgencyDao()
    }

    @Provides
    fun provideContractorDao(appDatabase: AppDatabase): ContractorDao {
        return appDatabase.getContractorDao()
    }

    @Provides
    fun provideOwnerDao(appDatabase: AppDatabase): OwnerDao {
        return appDatabase.getOwnerDao()
    }

    @Provides
    fun provideProperyDao(appDatabase: AppDatabase): PropertyDao {
        return appDatabase.getPropertyDao()
    }

    @Provides
    fun provideTenantDao(appDatabase: AppDatabase): TenantDao {
        return appDatabase.getTenantDao()
    }

    @Provides
    fun provideRoomReferenceDao(appDatabase: AppDatabase): RoomReferenceDao {
        return appDatabase.getRoomReferenceDao()
    }

    @Provides
    fun provideElementReferenceDao(appDatabase: AppDatabase): ElementReferenceDao {
        return appDatabase.getElementReferenceDao()
    }

    @Provides
    fun provideLotReferenceDao(appDatabase: AppDatabase): LotReferenceDao {
        return appDatabase.getLotReferenceDao()
    }

    @Provides
    fun provideDetailDao(appDatabase: AppDatabase): DetailDao {
        return appDatabase.getDetailDao()
    }

    @Provides
    fun provideEtatDao(appDatabase: AppDatabase): EtatDao {
        return appDatabase.getEtatDao()
    }

    @Provides
    fun provideAlterationDao(appDatabase: AppDatabase): AlterationDao {
        return appDatabase.getAlterationDao()
    }

    @Provides
    fun provideDescriptifDao(appDatabase: AppDatabase): DescriptifDao {
        return appDatabase.getDescriptifDao()
    }

    @Provides
    fun providePropreteDao(appDatabase: AppDatabase): PropreteDao {
        return appDatabase.getPropreteDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context,
        constatDao: Provider<ConstatDao>,
        propertyDao: Provider<PropertyDao>,
        agencyDao: Provider<AgencyDao>,
        tenantDao: Provider<TenantDao>,
        ownerDao: Provider<OwnerDao>,
        userDao: Provider<UserDao>,
        contractorDao: Provider<ContractorDao>,
        roomReferenceDao: Provider<RoomReferenceDao>,
        elementReferenceDao: Provider<ElementReferenceDao>,
        lotReferenceDao: Provider<LotReferenceDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(
                            constatDao.get(),
                            propertyDao.get(),
                            agencyDao.get(),
                            tenantDao.get(),
                            ownerDao.get(),
                            userDao.get(),
                            contractorDao.get(),
                            roomReferenceDao.get(),
                            elementReferenceDao.get(),
                            lotReferenceDao.get()
                        )
                    }
                }
            })
            .build()
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
        contractorDao: ContractorDao,
        roomReferenceDao: RoomReferenceDao,
        elementReferenceDao: ElementReferenceDao,
        lotReferenceDao: LotReferenceDao
    ) {
        // Delete all content
        constatDao.deleteAll()

        toDeleteForProd(
            constatDao,
            propertyDao,
            agencyDao,
            tenantDao,
            ownerDao,
            userDao,
            contractorDao
        )

        createRoomsReference(roomReferenceDao)
        createElementReferences(elementReferenceDao)
        createLotReferences(lotReferenceDao)

    }

    suspend fun createRoomsReference(roomReferenceDao: RoomReferenceDao) {
        ROOMS_LABELS.forEach {
            val roomReference = RoomReference(UUID.randomUUID().toString(), it)
            if (it == "ACCES / ENTREE") {
                roomReference.mandatory = true;
            }
            roomReferenceDao.save(roomReference)
        }
    }

    suspend fun createElementReferences(elementReferenceDao: ElementReferenceDao) {
        ELEMENTS_LABELS.forEach {
            val elementReference = ElementReference(UUID.randomUUID().toString(), it)
            elementReferenceDao.save(elementReference)
        }
    }

    suspend fun createLotReferences(lotReferenceDao: LotReferenceDao) {
        LOTS_LABELS.forEach {
            val lotReference = LotReference(UUID.randomUUID().toString(), it)
            lotReferenceDao.save(lotReference)
        }
    }

    suspend fun toDeleteForProd(
        constatDao: ConstatDao,
        propertyDao: PropertyDao,
        agencyDao: AgencyDao,
        tenantDao: TenantDao,
        ownerDao: OwnerDao,
        userDao: UserDao,
        contractorDao: ContractorDao
    ) {

        //Add Sample Constat
        val constat = Constat(
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