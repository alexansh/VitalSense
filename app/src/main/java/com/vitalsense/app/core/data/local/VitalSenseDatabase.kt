package com.vitalsense.app.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vitalsense.app.core.data.local.dao.VitalSenseDao
import com.vitalsense.app.core.data.local.entity.*
import com.vitalsense.app.core.data.local.typeconverters.Converters

import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        VillageEntity::class,
        PatientEntity::class,
        AshaWorkerEntity::class,
        DoctorEntity::class,
        ConditionRecordEntity::class,
        PrescriptionEntity::class,
        AppointmentEntity::class,
        BroadcastNoticeEntity::class,
        DispensaryEntity::class,
        GovernmentSchemeEntity::class,
        DepartmentEntity::class,
        ReferralEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VitalSenseDatabase : RoomDatabase() {

    abstract fun vitalSenseDao(): VitalSenseDao

    companion object {
        @Volatile
        private var INSTANCE: VitalSenseDatabase? = null

        fun getDatabase(context: Context): VitalSenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = "dummy_passphrase".toByteArray() // TODO: Fetch securely from Android Keystore
                val factory = SupportFactory(passphrase)
                
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalSenseDatabase::class.java,
                    "vitalsense_database"
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
