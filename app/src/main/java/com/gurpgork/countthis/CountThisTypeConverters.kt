package com.gurpgork.countthis

import androidx.room.TypeConverter
import java.time.*
import java.time.format.DateTimeFormatter

object CountThisTypeConverters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private val dayOfWeekValues by lazy(LazyThreadSafetyMode.NONE) { DayOfWeek.values() }

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

//    @TypeConverter
//    @JvmStatic
//    fun toOffsetDateTime(value: String?): OffsetDateTime? {
//        return value?.let { OffsetDateTime.parse(it) }
//    }

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
    fun toZoneOffset(value: String?) = value?.let { ZoneOffset.of(it) }

    @TypeConverter
    @JvmStatic
    fun fromZoneOffset(value: ZoneOffset?) = value?.id

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?) = value?.let { LocalTime.parse(value) }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?) = value?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(value) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

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
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun toDuration(value: Long?): Duration? {
        return value?.let { Duration.ofMillis(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDuration(value: Duration?): Long? {
        return value?.toMillis()
    }
}