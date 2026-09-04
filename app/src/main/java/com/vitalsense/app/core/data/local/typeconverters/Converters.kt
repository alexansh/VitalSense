package com.vitalsense.app.core.data.local.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vitalsense.app.core.data.model.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = runCatching { UserRole.valueOf(value) }.getOrDefault(UserRole.PATIENT)

    @TypeConverter
    fun fromSeverityLevel(value: SeverityLevel): String = value.name

    @TypeConverter
    fun toSeverityLevel(value: String): SeverityLevel = runCatching { SeverityLevel.valueOf(value) }.getOrDefault(SeverityLevel.LOW)

    @TypeConverter
    fun fromConditionCategory(value: ConditionCategory): String = value.name

    @TypeConverter
    fun toConditionCategory(value: String): ConditionCategory = runCatching { ConditionCategory.valueOf(value) }.getOrDefault(ConditionCategory.GENERAL_MEDICINE)

    @TypeConverter
    fun fromDoctorSpecialty(value: DoctorSpecialty): String = value.name

    @TypeConverter
    fun toDoctorSpecialty(value: String): DoctorSpecialty = runCatching { DoctorSpecialty.valueOf(value) }.getOrDefault(DoctorSpecialty.GENERAL_PHYSICIAN)

    @TypeConverter
    fun fromCaseStatus(value: CaseStatus): String = value.name

    @TypeConverter
    fun toCaseStatus(value: String): CaseStatus = runCatching { CaseStatus.valueOf(value) }.getOrDefault(CaseStatus.PENDING_REVIEW)

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromVaccineInfoList(value: List<VaccineInfo>): String = gson.toJson(value)

    @TypeConverter
    fun toVaccineInfoList(value: String): List<VaccineInfo> {
        val listType = object : TypeToken<List<VaccineInfo>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromLabTestItemList(value: List<LabTestItem>): String = gson.toJson(value)

    @TypeConverter
    fun toLabTestItemList(value: String): List<LabTestItem> {
        val listType = object : TypeToken<List<LabTestItem>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromQueueEntrySource(value: QueueEntrySource): String = value.name

    @TypeConverter
    fun toQueueEntrySource(value: String): QueueEntrySource = runCatching { QueueEntrySource.valueOf(value) }.getOrDefault(QueueEntrySource.WALK_IN)

    @TypeConverter
    fun fromQueueEntryStatus(value: QueueEntryStatus): String = value.name

    @TypeConverter
    fun toQueueEntryStatus(value: String): QueueEntryStatus = runCatching { QueueEntryStatus.valueOf(value) }.getOrDefault(QueueEntryStatus.WAITING)

    @TypeConverter
    fun fromReferralUrgency(value: ReferralUrgency): String = value.name

    @TypeConverter
    fun toReferralUrgency(value: String): ReferralUrgency = runCatching { ReferralUrgency.valueOf(value) }.getOrDefault(ReferralUrgency.ROUTINE)

    @TypeConverter
    fun fromReferralStatus(value: ReferralStatus): String = value.name

    @TypeConverter
    fun toReferralStatus(value: String): ReferralStatus = runCatching { ReferralStatus.valueOf(value) }.getOrDefault(ReferralStatus.CREATED)

    @TypeConverter
    fun fromReferralStatusHistoryList(value: List<ReferralStatusHistory>): String = gson.toJson(value)

    @TypeConverter
    fun toReferralStatusHistoryList(value: String): List<ReferralStatusHistory> {
        val listType = object : TypeToken<List<ReferralStatusHistory>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}
