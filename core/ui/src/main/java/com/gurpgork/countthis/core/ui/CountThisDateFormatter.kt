package com.gurpgork.countthis.core.ui

import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class CountThisDateFormatter @Inject constructor(
    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
//    @ShortDate private val shortDateFormatter: DateTimeFormatter,
    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter,
) {
    private val locale: Locale = Locale.getDefault()
    internal val timeZone: TimeZone = TimeZone.currentSystemDefault()

    private val shortDateFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(locale)
            .withZone(timeZone.toJavaZoneId())
    }
    //TODO use these if decide don't want @ShortTime and other stuff
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

    private val dayOfWeekFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("EEEE")
            .withLocale(locale)
            .withZone(timeZone.toJavaZoneId())
    }
    private fun Instant.toTemporal(): Temporal{
        return LocalDateTime.ofInstant(toJavaInstant(), timeZone.toJavaZoneId())
    }


    fun formatShortTime(instant: Instant): String {
        return shortTimeFormatter.format(instant.toTemporal())
    }
    fun formatShortDate(instant: Instant): String{
        return shortDateFormatter.format(instant.toTemporal())
    }
    fun formatMediumDate(instant: Instant): String{
        return mediumDateFormatter.format(instant.toTemporal())
    }
    fun formatMediumDateTime(instant: Instant): String {
        return mediumDateTimeFormatter.format(instant.toTemporal())
    }

    fun formatShortRelativeTime(date: Instant, reference: Instant = Clock.System.now()): String = when{
        // within past week
        date < reference && (reference - date) < 7.days -> {
            DateUtils.getRelativeTimeSpanString(
                date.toEpochMilliseconds(),
                reference.toEpochMilliseconds(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_SHOW_DATE,
            ).toString()
        }
        date > reference && (date - reference) < 14.days -> {
            DateUtils.getRelativeTimeSpanString(
                date.toEpochMilliseconds(),
                reference.toEpochMilliseconds(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_SHOW_DATE,
            ).toString()
        }
        else -> formatShortDate(date)
    }

    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String {
        return Clock.System.now()
            .toLocalDateTime(timeZone)
            .toJavaLocalDateTime()
            .with(TemporalAdjusters.nextOrSame(dayOfWeek.toJavaDayOfWeek()))
            .let { dayOfWeekFormatter.format(it) }
    }

//    fun formatShortDate(temporalAmount: Temporal): String = shortDateFormatter.format(temporalAmount)
//
//    fun formatMediumDate(temporalAmount: Temporal): String = mediumDateFormatter.format(temporalAmount)
//
//    fun formatMediumDateTime(temporalAmount: Temporal): String = mediumDateTimeFormatter.format(temporalAmount)

    @Composable
    fun dateFormatted(publishedDate: Instant): String{
        var zoneId by remember{ mutableStateOf(ZoneId.systemDefault()) }
        val context = LocalContext.current

        DisposableEffect(context){
            val receiver = TimeZoneBroadcastReceiver(
                onTimeZoneChanged = { zoneId = ZoneId.systemDefault() },
            )
            receiver.register(context)
            onDispose {
                receiver.unregister(context)
            }
        }

        return DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .withZone(zoneId)
            .format(publishedDate.toJavaInstant())
    }
}
private fun DayOfWeek.toJavaDayOfWeek(): java.time.DayOfWeek = when (this) {
    java.time.DayOfWeek.MONDAY -> DayOfWeek.MONDAY
    java.time.DayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
    java.time.DayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
    java.time.DayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
    java.time.DayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
    java.time.DayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
    java.time.DayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
}
