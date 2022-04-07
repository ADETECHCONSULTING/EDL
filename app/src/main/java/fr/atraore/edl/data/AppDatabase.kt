package fr.atraore.edl.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.atraore.edl.data.dao.*
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.utils.*

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
        ConstatLotCrossRef::class,
        RoomDetailCrossRef::class,
        Owner::class,
        Property::class,
        Tenant::class,
        Contractor::class,
        Agency::class,
        Users::class,
        RoomReference::class,
        ElementReference::class,
        LotReference::class,
        Alteration::class,
        Descriptif::class,
        Etat::class,
        Proprete::class,
        Detail::class,
        Compteur::class,
        PhotoCompteur::class,
        CompteurReference::class,
        ConfigPdf::class,
        RoomElementCrossRef::class,
        KeyReference::class,
        ConstatKey::class
    ], version = 1, exportSchema = false
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
    abstract fun getDetailDao(): DetailDao
    abstract fun getEtatDao(): EtatDao
    abstract fun getDescriptifDao(): DescriptifDao
    abstract fun getPropreteDao(): PropreteDao
    abstract fun getAlterationDao(): AlterationDao
    abstract fun getCompteurReferenceDao(): CompteurReferenceDao
    abstract fun getCompteurDao(): CompteurDao
    abstract fun getPhotoCompteurDao(): PhotoCompteurDao
    abstract fun getConfigPdfDao(): ConfigPdfDao
    abstract fun getKeyDao(): KeyDao
    abstract fun getConstatKeyDao(): ConstatKeyDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null
    }

}