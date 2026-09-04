package com.vitalsense.app.core.di

import android.content.Context
import com.vitalsense.app.core.data.local.VitalSenseDatabase
import com.vitalsense.app.core.data.repository.VitalSenseRepository
import com.vitalsense.app.core.data.repository.VitalSenseRepositoryImpl
import com.vitalsense.app.core.data.remote.FirestoreDataSource
import com.vitalsense.app.core.network.NetworkMonitor
import com.vitalsense.app.core.sync.SyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVitalSenseDatabase(
        @ApplicationContext context: Context
    ): VitalSenseDatabase {
        return VitalSenseDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideVitalSenseRepository(
        database: VitalSenseDatabase,
        firestoreDataSource: FirestoreDataSource,
        syncManager: SyncManager,
        networkMonitor: NetworkMonitor
    ): VitalSenseRepository {
        return VitalSenseRepositoryImpl(database, firestoreDataSource, syncManager, networkMonitor)
    }

    @Provides
    @Singleton
    fun provideMedicineAvailabilityRepository(
        database: VitalSenseDatabase,
        @ApplicationContext context: Context
    ): com.vitalsense.app.core.data.medicine.MedicineAvailabilityRepository {
        return com.vitalsense.app.core.data.medicine.MedicineAvailabilityRepositoryImpl(database, context)
    }
}
