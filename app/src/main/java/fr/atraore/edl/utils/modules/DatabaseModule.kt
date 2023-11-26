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
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.sql.Date
import java.util.*
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
    fun provideCompteurDao(appDatabase: AppDatabase): CompteurDao {
        return appDatabase.getCompteurDao()
    }

    @Provides
    fun provideCompteurReferenceDao(appDatabase: AppDatabase): CompteurReferenceDao {
        return appDatabase.getCompteurReferenceDao()
    }

    @Provides
    fun providePhotoCompteurDao(appDatabase: AppDatabase): PhotoCompteurDao {
        return appDatabase.getPhotoCompteurDao()
    }

    @Provides
    fun provideConfigPdfDao(appDatabase: AppDatabase): ConfigPdfDao {
        return appDatabase.getConfigPdfDao()
    }

    @Provides
    fun provideKeyDao(appDatabase: AppDatabase): KeyDao {
        return appDatabase.getKeyDao()
    }

    @Provides
    fun provideConstatKeyDao(appDatabase: AppDatabase): ConstatKeyDao {
        return appDatabase.getConstatKeyDao()
    }

    @Provides
    fun provideOutDoorEquipementDao(appDatabase: AppDatabase): OutDoorEquipementDao {
        return appDatabase.getOutDoorEquipementDao()
    }

    @Provides
    fun provideEquipmentDao(appDatabase: AppDatabase): EquipmentDao {
        return appDatabase.getEquipmentDao()
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
        lotReferenceDao: Provider<LotReferenceDao>,
        compteurReferenceDao: Provider<CompteurReferenceDao>,
        configPdfDao: Provider<ConfigPdfDao>,
        keyDao: Provider<KeyDao>,
        outDoorEquipementDao: Provider<OutDoorEquipementDao>,
        propreteDao: Provider<PropreteDao>
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
                        executeSQLFromFile(applicationContext, db, "Equipments_202310222323.sql")
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
                            lotReferenceDao.get(),
                            compteurReferenceDao.get(),
                            configPdfDao.get(),
                            keyDao.get(),
                            outDoorEquipementDao.get(),
                            propreteDao.get()
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
        //
        roomReferenceDao: RoomReferenceDao,
        elementReferenceDao: ElementReferenceDao,
        lotReferenceDao: LotReferenceDao,
        compteurReferenceDao: CompteurReferenceDao,
        configPdfDao: ConfigPdfDao,
        keyDao: KeyDao,
        outDoorEquipementDao: OutDoorEquipementDao,
        propreteDao: PropreteDao
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
        createLotReferences(lotReferenceDao, elementReferenceDao)
        createCompteurReferences(compteurReferenceDao)
        createConfigPdfReference(configPdfDao)
        createKeyReferences(keyDao)
        createOutDoorEquipementReferences(outDoorEquipementDao)
        createPropreteReferences(propreteDao)
    }

    private suspend fun createRoomsReference(roomReferenceDao: RoomReferenceDao) {
        ROOMS_LABELS.forEach {
            val roomReference = RoomReference(null, UUID.randomUUID().toString(), it)
            if (it == "ACCES/ENTREE") {
                roomReference.mandatory = true
            }
            roomReferenceDao.save(roomReference)
        }
    }

    private suspend fun createLotReferences(lotReferenceDao: LotReferenceDao, elementReferenceDao: ElementReferenceDao) {
        for ((index, value) in LOTS_LABELS.withIndex()) {
            val lotReference = LotReference(index+1, value)
            lotReferenceDao.save(lotReference)

            when (lotReference.lotReferenceId) {
                1 -> { //revetement
                    saveElementRefs(ELEMENTS_REVETEMENTS_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                2 -> { //ouvrants
                    saveElementRefs(ELEMENTS_OUVRANTS_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                3 -> { //electricité
                    saveElementRefs(ELEMENTS_ELEC_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                4 -> { //plomberie
                    saveElementRefs(ELEMENTS_PLOMBERIE_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                5 -> { //chauffage
                    saveElementRefs(ELEMENTS_CHAUFFAGE_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                6 -> { //electroménager
                    saveElementRefs(ELEMENTS_ELECTRONIQUE_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
                7 -> { //Rangement
                    saveElementRefs(ELEMENTS_RANGEMENT_LABELS, lotReference.lotReferenceId, elementReferenceDao)
                }
            }
        }
    }

    private suspend fun createCompteurReferences(compteurReferenceDao: CompteurReferenceDao) {
        for ((index, value) in COMPTEUR_LABELS.withIndex()) {
            val compteurReference = CompteurReference(index+1, value)
            compteurReferenceDao.save(compteurReference)
        }
    }

    private suspend fun createPropreteReferences(propreteDao: PropreteDao) {
        PROPRETE_LABELS.forEach {
            val proprete = Proprete(UUID.randomUUID().toString(), it)
            propreteDao.save(proprete)
        }
    }

    private suspend fun createConfigPdfReference(configPdfDao: ConfigPdfDao) {
        configPdfDao.save(
            ConfigPdf(
            "Pré-état des lieux de sortie",
            "Les soussignés reconnaissent exactes les constatations sur l'état du logement,\n" +
                    " sous réserve du bon fonctionnement des canalisations,\n" +
                    " appareils et installations sanitaires, électriques et du chauffage qui n'a pu être vérifié ce jour, toute défectuosité dans le fonctionnement\n" +
                    "  de ceux-ci devant être signalée dans le délai maximum de huit jours, et pendant le premier mois de la période de chauffe en ce qui\n" +
                    "  concerne les éléments de chauffage.\n",
            "Les co-signataires reconnaissent avoir reçu chacun un exemplaire du présent état des lieux et s'accordent pour y faire référence lors du\n" +
                    " départ du locataire\n",
            "Les soussignés reconnaissent exactes les constatations sur l'état du logement,\n" +
                    " et reconnaissent avoir reçu chacun l'ensemble des élements leur permettant de récupérer un exemplaire du présent état des lieux et s'accordent pour y faire référence.\n",
            "Le présent état des lieux, a été établi contradictoirement entre les parties qui le reconnaissent exact."
        )
        )
    }

    private suspend fun createKeyReferences(keyDao: KeyDao) {
        for (value in KEYS_LABELS) {
            val keyReference = KeyReference(value, true)
            keyDao.save(keyReference)
        }
    }


    private suspend fun createOutDoorEquipementReferences(outDoorEquipementDao: OutDoorEquipementDao) {
        for (value in OUTDOORS_EQUIPMNT_LABELS) {
            val outdoorRef = OutdoorEquipementReference(value, true)
            outDoorEquipementDao.save(outdoorRef)
        }
    }

    private suspend fun toDeleteForProd(
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
            dateCreation = Date(System.currentTimeMillis()),
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
            "",
            1,
            null
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
            "0627562283",
            "",
            "18 rue Léandre vaillat",
            "",
            "",
            "",
            "74100",
            "",
            "Annemasse",
            "",
            ""
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
            Date(System.currentTimeMillis())
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
            Date(System.currentTimeMillis())
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

    suspend fun saveElementRefs(liste: Array<*>, idLot: Int, dao: ElementReferenceDao) {
        val listToSave = mutableListOf<ElementReference>()
        for (element in liste) {
            if (element is List<*>) {
                val firstItem = element.firstOrNull()
                firstItem?.let {
                    val firstItemRef = ElementReference(UUID.randomUUID().toString(), firstItem as String, false, idLot)
                    listToSave.add(firstItemRef)
                    for (j in 1 until element.size) {
                        val sousElement = element[j]
                        if (sousElement is String) {
                            val itemRef = ElementReference(UUID.randomUUID().toString(), sousElement, false, idLot, firstItemRef.elementReferenceId)
                            listToSave.add(itemRef)
                        }
                    }
                }
            } else {
                listToSave.add(ElementReference(UUID.randomUUID().toString(), element as String, false, idLot))
            }
        }
        if (listToSave.isNotEmpty()) {
            listToSave.forEach {
                dao.save(it)
            }
        }
    }

    fun readSQLFromAssets(context: Context, fileName: String): String {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }

    fun executeSQLFromFile(context: Context, database: SupportSQLiteDatabase, fileName: String) {
        val sqlStatements = readSQLFromAssets(context, fileName).split(";")
        for (statement in sqlStatements) {
            if (statement.trim().isNotEmpty()) {
                database.execSQL(statement)
            }
        }
    }



}