package com.vitalsense.app.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitalsense.app.core.data.local.dao.VitalSenseDao
import com.vitalsense.app.core.data.local.entity.*
import com.vitalsense.app.core.data.local.typeconverters.Converters

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
        OutboxEntity::class,
        ImmunizationRecordEntity::class,
        DailyRoundEntity::class,
        AshaMedicineEntity::class,
        DiseaseTrendRecordEntity::class,
        LabReportEntity::class,
        OpdTokenEntity::class,
        MedicalCertificateEntity::class,
        BloodStockEntity::class,
        IpdBedEntity::class,
        OtSurgeryBookingEntity::class,
        ExternalReferralEntity::class,
        BioMedicalEquipmentEntity::class,
        DoctorDaySlotEntity::class,
        QueueEntryEntity::class,
        MedicalHistoryEntity::class,
        NearbyPharmacyCacheEntity::class,
        CallLogEntity::class,
        ReferralEntity::class,
        AuditLogEntity::class
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VitalSenseDatabase : RoomDatabase() {

    abstract fun vitalSenseDao(): VitalSenseDao

    companion object {
        @Volatile
        private var INSTANCE: VitalSenseDatabase? = null

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `doctor_day_slots` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `doctorId` TEXT NOT NULL,
                        `dateFormatted` TEXT NOT NULL,
                        `startTime` TEXT NOT NULL,
                        `endTime` TEXT NOT NULL,
                        `capacity` INTEGER NOT NULL,
                        `isWalkInOpen` INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `queue_entries` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `doctorId` TEXT NOT NULL,
                        `doctorName` TEXT NOT NULL,
                        `dateFormatted` TEXT NOT NULL,
                        `tokenNumber` INTEGER NOT NULL,
                        `provisionalToken` INTEGER NOT NULL,
                        `appointmentId` TEXT,
                        `patientId` TEXT NOT NULL,
                        `patientName` TEXT NOT NULL,
                        `source` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `priorityFlag` INTEGER NOT NULL,
                        `checkedInAt` INTEGER NOT NULL,
                        `calledAt` INTEGER,
                        `consultationStartedAt` INTEGER,
                        `completedAt` INTEGER,
                        `outcomeNotes` TEXT,
                        `isPendingSync` INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `nearby_pharmacy_cache` (
                        `placeId` TEXT NOT NULL PRIMARY KEY,
                        `name` TEXT NOT NULL,
                        `address` TEXT NOT NULL,
                        `latitude` REAL NOT NULL,
                        `longitude` REAL NOT NULL,
                        `phoneNumber` TEXT,
                        `cachedAt` INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `appointments` ADD COLUMN `callType` TEXT NOT NULL DEFAULT 'VIDEO'")
                db.execSQL("ALTER TABLE `appointments` ADD COLUMN `scheduledTimestamp` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `call_logs` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `callType` TEXT NOT NULL,
                        `callMode` TEXT NOT NULL,
                        `patientId` TEXT NOT NULL,
                        `patientName` TEXT NOT NULL,
                        `doctorId` TEXT NOT NULL,
                        `doctorName` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `durationSeconds` INTEGER NOT NULL,
                        `outcome` TEXT NOT NULL,
                        `outcomeNotes` TEXT
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `referrals` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `patientId` TEXT NOT NULL,
                        `patientName` TEXT NOT NULL,
                        `referringDoctorId` TEXT NOT NULL,
                        `referringDoctorName` TEXT NOT NULL,
                        `referringDoctorSpecialty` TEXT NOT NULL,
                        `targetDoctorId` TEXT,
                        `targetDoctorName` TEXT,
                        `targetSpecialty` TEXT NOT NULL,
                        `reason` TEXT NOT NULL,
                        `clinicalQuestion` TEXT NOT NULL,
                        `urgency` TEXT NOT NULL,
                        `attachedRecordIds` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `declineReason` TEXT,
                        `suggestedSpecialtyOrDoctor` TEXT,
                        `infoRequestNote` TEXT,
                        `specialistFindings` TEXT,
                        `specialistRecommendations` TEXT,
                        `specialistFollowUpNeeded` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `respondedAt` INTEGER,
                        `completedAt` INTEGER
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_patientId` ON `referrals` (`patientId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_referringDoctorId` ON `referrals` (`referringDoctorId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_targetDoctorId` ON `referrals` (`targetDoctorId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_targetSpecialty` ON `referrals` (`targetSpecialty`)")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `medical_history` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `patientId` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `details` TEXT NOT NULL,
                        `severity` TEXT,
                        `doctorId` TEXT NOT NULL,
                        `doctorName` TEXT NOT NULL,
                        `caseId` TEXT,
                        `prescriptionId` TEXT,
                        `timestamp` INTEGER NOT NULL,
                        `dateFormatted` TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `audit_logs` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `timestamp` INTEGER NOT NULL,
                        `actorId` TEXT NOT NULL,
                        `actorRole` TEXT NOT NULL,
                        `action` TEXT NOT NULL,
                        `resourceId` TEXT,
                        `resourceType` TEXT,
                        `details` TEXT,
                        `isSynced` INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `referrals_new` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `patientId` TEXT NOT NULL,
                        `patientName` TEXT NOT NULL,
                        `referringUserId` TEXT NOT NULL,
                        `referringUserName` TEXT NOT NULL,
                        `referringUserSpecialty` TEXT NOT NULL,
                        `targetDoctorId` TEXT,
                        `targetDoctorName` TEXT,
                        `targetSpecialty` TEXT NOT NULL,
                        `reason` TEXT NOT NULL,
                        `clinicalQuestion` TEXT NOT NULL,
                        `urgency` TEXT NOT NULL,
                        `attachedRecordIds` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `statusHistory` TEXT NOT NULL,
                        `declineReason` TEXT,
                        `suggestedSpecialtyOrDoctor` TEXT,
                        `infoRequestNote` TEXT,
                        `specialistFindings` TEXT,
                        `specialistRecommendations` TEXT,
                        `specialistFollowUpNeeded` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `respondedAt` INTEGER,
                        `completedAt` INTEGER
                    )
                """.trimIndent())

                // Copy the data
                db.execSQL("""
                    INSERT INTO `referrals_new` (
                        `id`, `patientId`, `patientName`, `referringUserId`, `referringUserName`, `referringUserSpecialty`, 
                        `targetDoctorId`, `targetDoctorName`, `targetSpecialty`, `reason`, `clinicalQuestion`, 
                        `urgency`, `attachedRecordIds`, `status`, `statusHistory`, `declineReason`, `suggestedSpecialtyOrDoctor`, 
                        `infoRequestNote`, `specialistFindings`, `specialistRecommendations`, `specialistFollowUpNeeded`, 
                        `createdAt`, `updatedAt`, `respondedAt`, `completedAt`
                    )
                    SELECT 
                        `id`, `patientId`, `patientName`, `referringDoctorId`, `referringDoctorName`, `referringDoctorSpecialty`, 
                        `targetDoctorId`, `targetDoctorName`, `targetSpecialty`, `reason`, `clinicalQuestion`, 
                        `urgency`, `attachedRecordIds`, `status`, '[]', `declineReason`, `suggestedSpecialtyOrDoctor`, 
                        `infoRequestNote`, `specialistFindings`, `specialistRecommendations`, `specialistFollowUpNeeded`, 
                        `createdAt`, `createdAt`, `respondedAt`, `completedAt`
                    FROM `referrals`
                """.trimIndent())

                // Drop the old table
                db.execSQL("DROP TABLE `referrals`")

                // Rename the new table
                db.execSQL("ALTER TABLE `referrals_new` RENAME TO `referrals`")

                // Re-create indices
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_patientId` ON `referrals` (`patientId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_referringUserId` ON `referrals` (`referringUserId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_targetDoctorId` ON `referrals` (`targetDoctorId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_referrals_targetSpecialty` ON `referrals` (`targetSpecialty`)")
            }
        }

        fun getDatabase(context: Context): VitalSenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalSenseDatabase::class.java,
                    "vitalsense_database"
                )
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
