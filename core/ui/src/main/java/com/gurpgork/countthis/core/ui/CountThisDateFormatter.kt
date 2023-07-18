package com.gurpgork.countthis.core.ui

import android.text.format.DateUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountThisDateFormatter @Inject constructor(
    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
    @ShortDate private val shortDateFormatter: DateTimeFormatter,
    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter
) {
    fun formatShortDate(temporalAmount: Temporal): String = shortDateFormatter.format(temporalAmount)

    fun formatMediumDate(temporalAmount: Temporal): String = mediumDateFormatter.format(temporalAmount)

    fun formatMediumDateTime(temporalAmount: Temporal): String = mediumDateTimeFormatter.format(temporalAmount)

    fun formatShortTime(localTime: LocalTime): String = shortTimeFormatter.format(localTime)

    fun formatShortRelativeTime(dateTime: OffsetDateTime): String {
        val now = OffsetDateTime.now()

        return if (dateTime.isBefore(now)) {
            if (dateTime.year == now.year || dateTime.isAfter(now.minusDays(7))) {
                // Within the past week
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toInstant().toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // More than 7 days ago
                formatShortDate(dateTime)
            }
        } else {
            if (dateTime.year == now.year || dateTime.isBefore(now.plusDays(14))) {
                // In the near future (next 2 weeks)
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toInstant().toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // In the far future
                formatShortDate(dateTime)
            }
        }
    }
    fun formatShortRelativeTime(instant: Instant): String {
        val givenDate = LocalDateTime.from(instant)
        val nowDate = LocalDateTime.now()

        return if (instant.isBefore(Instant.now())) {
            if (givenDate.year == nowDate.year || givenDate.isAfter(nowDate.minusDays(7))) {
                // Within the past week
                DateUtils.getRelativeTimeSpanString(
                    instant.toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // More than 7 days ago
                formatShortDate(instant)
            }
        } else {
            if (givenDate.year == nowDate.year || givenDate.isBefore(nowDate.plusDays(14))) {
                // In the near future (next 2 weeks)
                DateUtils.getRelativeTimeSpanString(
                    instant.toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // In the far future
                formatShortDate(instant)
            }
        }
    }

    fun formatShortRelativeTime(instant: Instant, zoneId: ZoneId): String {
        val zdt = instant.atZone(zoneId)
        val nzdt = ZonedDateTime.now(zoneId)

//        val odt = instant.atOffset(ZoneOffset.UTC)
//        val nodt = OffsetDateTime.now()
//        val givenDate = LocalDateTime.from(instant)
//        val nowDate = LocalDateTime.now()

        return if (instant.isBefore(Instant.now())) {
            if (zdt.year == nzdt.year || zdt.isAfter(nzdt.minusDays(7))) {
                // Within the past week
                DateUtils.getRelativeTimeSpanString(
                    zdt.toEpochSecond() * 1000,//instant.toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // More than 7 days ago
                formatShortDate(instant)
            }
        } else {
            if (zdt.year == nzdt.year || zdt.isBefore(nzdt.plusDays(14))) {
                // In the near future (next 2 weeks)
                DateUtils.getRelativeTimeSpanString(
                    instant.toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // In the far future
                formatShortDate(instant)
            }
        }
    }

    fun formatShortRelativeTime(instant: Instant, zoneOffset: ZoneOffset): String {
        val odt = instant.atOffset(zoneOffset)

        return formatShortRelativeTime(odt)
    }
}
