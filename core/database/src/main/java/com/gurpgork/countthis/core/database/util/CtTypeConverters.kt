package com.gurpgork.countthis.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object CtTypeConverters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private val dayOfWeekValues by lazy(LazyThreadSafetyMode.NONE) { DayOfWeek.values() }


    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let { OffsetDateTime.parse(it) }
    }

    //    @TypeConverter
//    @JvmStatic
//    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }
//
    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun toZoneId(value: String?) = value?.let { ZoneId.of(it) }

    @TypeConverter
    @JvmStatic
    fun fromZoneId(value: ZoneId?) = value?.id

    @TypeConverter
    @JvmStatic
    fun fromZoneOffset(value: ZoneOffset) = value.id
    @TypeConverter
    @JvmStatic
    fun toZoneOffset(value: String?) = value.let { ZoneOffset.of(it) }

//    @TypeConverter
//    @JvmStatic
//    fun toFixedOffsetTimeZone(value: String?) = value?.let { FixedOffsetTimeZone.serializer() }
//
//    @TypeConverter
//    @JvmStatic
//    fun fromFixedOffsetTimeZone(value: FixedOffsetTimeZone?) = value?.id
//
//    @TypeConverter
//    @JvmStatic
//    fun toLocalTime(value: String?) = value?.let { LocalTime.parse(value) }
//
//    @TypeConverter
//    @JvmStatic
//    fun toLocalDateTime(value: String?): LocalDateTime? {
//        return value?.let { LocalDateTime.parse(value) }
//    }

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(value: Int?): DayOfWeek? {
        return if (value != null) {
            dayOfWeekValues.firstOrNull { it.value == value }
        } else null
    }

    @TypeConverter
    @JvmStatic
    fun fromDayOfWeek(day: DayOfWeek?) = day?.value


    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilliseconds()
//
//
//    @TypeConverter
//    @JvmStatic
//    fun fromInstant(date: Instant?) = date?.toJavaInstant()
//
//    @TypeConverter
//    @JvmStatic
//    fun toDuration(value: Long?): Duration? {
//        return value?.milliseconds
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun fromDuration(value: Duration?): Long? {
//        return value?.inWholeMilliseconds
//    }


//    @TypeConverter
//    @JvmStatic
//    fun fromLocalTime(value: LocalTime?) = value?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    //    @TypeConverter
//    @JvmStatic
//    fun fromLocalDateTime(value: LocalDateTime?): String? {
//        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//    }
//    @TypeConverter
//    @JvmStatic
//    fun toInstant(value: Long?) = value?.let { Instant.fromEpochMilliseconds(it) }
}