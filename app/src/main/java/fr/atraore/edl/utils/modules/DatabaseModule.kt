package fr.atraore.edl.utils.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.atraore.edl.data.AppDatabase
import fr.atraore.edl.data.DATABASE_NAME
import fr.atraore.edl.data.dao.*
import fr.atraore.edl.repository.PropertyRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideConstatDao(appDatabase: AppDatabase): ConstatDao {
        return appDatabase.getConstatDao();
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.getUserDao();
    }

    @Provides
    fun provideAgencyDao(appDatabase: AppDatabase): AgencyDao {
        return appDatabase.getAgencyDao();
    }

    @Provides
    fun provideContractorDao(appDatabase: AppDatabase): ContractorDao {
        return appDatabase.getContractorDao();
    }

    @Provides
    fun provideOwnerDao(appDatabase: AppDatabase): OwnerDao {
        return appDatabase.getOwnerDao();
    }

    @Provides
    fun provideProperyDao(appDatabase: AppDatabase): PropertyDao {
        return appDatabase.getPropertyDao();
    }

    @Provides
    fun provideTenantDao(appDatabase: AppDatabase): TenantDao {
        return appDatabase.getTenantDao();
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}