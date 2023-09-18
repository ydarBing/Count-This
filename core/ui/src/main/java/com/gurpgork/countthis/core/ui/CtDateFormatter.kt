package com.gurpgork.countthis.core.ui

//import android.text.format.DateUtils
//import dagger.hilt.android.scopes.ActivityScoped
//import kotlinx.datetime.Clock
//import kotlinx.datetime.DayOfWeek
//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.LocalTime
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toJavaInstant
//import kotlinx.datetime.toJavaLocalDate
//import kotlinx.datetime.toJavaLocalDateTime
//import kotlinx.datetime.toJavaLocalTime
//import kotlinx.datetime.toJavaZoneId
//import kotlinx.datetime.toLocalDateTime
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.time.format.FormatStyle
//import java.time.temporal.Temporal
//import java.time.temporal.TemporalAdjusters
//import java.util.Locale
//import javax.inject.Inject
//import kotlin.time.Duration.Companion.days
//
//@ActivityScoped
//@Inject
//class CtDateFormatter(
//    private val locale: Locale =  Locale.getDefault(),
//    internal val timeZone: TimeZone = TimeZone.currentSystemDefault(),
//){
//    private val shortDateFormatter: DateTimeFormatter by lazy {
//        DateTimeFormatter
//            .ofLocalizedDate(FormatStyle.SHORT)
//            .withLocale(locale)
//            .withZone(timeZone.toJavaZoneId())
//    }
//    private val shortTimeFormatter: DateTimeFormatter by lazy {
//        DateTimeFormatter
//            .ofLocalizedTime(FormatStyle.SHORT)
//            .withLocale(locale)
//            .withZone(timeZone.toJavaZoneId())
//    }
//    private val mediumDateFormatter: DateTimeFormatter by lazy {
//        DateTimeFormatter
//            .ofLocalizedDate(FormatStyle.MEDIUM)
//            .withLocale(locale)
//            .withZone(timeZone.toJavaZoneId())
//    }
//    private val mediumDateTimeFormatter: DateTimeFormatter by lazy {
//        DateTimeFormatter
//            .ofLocalizedDateTime(FormatStyle.MEDIUM)
//            .withLocale(locale)
//            .withZone(timeZone.toJavaZoneId())
//    }
//    private val dayOfWeekFormatter: DateTimeFormatter by lazy {
//        DateTimeFormatter.ofPattern("EEEE")
//            .withLocale(locale)
//            .withZone(timeZone.toJavaZoneId())
//    }
//
//    private fun Instant.toTemporal(): Temporal {
//        return LocalDateTime.ofInstant(toJavaInstant(), timeZone.toJavaZoneId())
//    }
//
//    fun formatShortDate(instant: Instant): String {
//        return shortDateFormatter.format(instant.toTemporal())
//    }
//
//    fun formatShortDate(date: LocalDate): String {
//        return shortDateFormatter.format(date.toJavaLocalDate())
//    }
//
//    fun formatMediumDate(instant: Instant): String {
//        return mediumDateFormatter.format(instant.toTemporal())
//    }
//
//    fun formatMediumDateTime(instant: Instant): String {
//        return mediumDateTimeFormatter.format(instant.toTemporal())
//    }
//
//    fun formatShortTime(localTime: LocalTime): String {
//        return shortTimeFormatter.format(localTime.toJavaLocalTime())
//    }
//
//    fun formatShortRelativeTime(date: Instant, reference: Instant): String = when {
//        // Within the past week
//        date < reference && (reference - date) < 7.days -> {
//            DateUtils.getRelativeTimeSpanString(
//                date.toEpochMilliseconds(),
//                reference.toEpochMilliseconds(),
//                DateUtils.MINUTE_IN_MILLIS,
//                DateUtils.FORMAT_SHOW_DATE,
//            ).toString()
//        }
//        // In the near future (next 2 weeks)
//        date > reference && (date - reference) < 14.days -> {
//            DateUtils.getRelativeTimeSpanString(
//                date.toEpochMilliseconds(),
//                reference.toEpochMilliseconds(),
//                DateUtils.MINUTE_IN_MILLIS,
//                DateUtils.FORMAT_SHOW_DATE,
//            ).toString()
//        }
//        // In the far past/future
//        else -> formatShortDate(date)
//    }
//
//    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String {
//        return Clock.System.now()
//            .toLocalDateTime(timeZone)
//            .toJavaLocalDateTime()
//            .with(TemporalAdjusters.nextOrSame(dayOfWeek.toJavaDayOfWeek()))
//            .let { dayOfWeekFormatter.format(it) }
//    }
//}
//
//private fun DayOfWeek.toJavaDayOfWeek(): java.time.DayOfWeek = when (this) {
//    java.time.DayOfWeek.MONDAY -> DayOfWeek.MONDAY
//    java.time.DayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
//    java.time.DayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
//    java.time.DayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
//    java.time.DayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
//    java.time.DayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
//    java.time.DayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
//}