package xyz.nejcrozman.progress.shared

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochSecond(it),
                ZoneId.systemDefault()
            )
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now()))
    }
    companion object {
        @TypeConverter
        fun dateToDateTime(date: LocalDate): LocalDateTime {
            return date.let { LocalDateTime.of(it, LocalTime.now()) }
        }

        @TypeConverter
        fun dateTimeToDate(dateTime: LocalDateTime): LocalDate {
            return dateTime.let { LocalDate.of(it.year, it.monthValue, it.dayOfMonth) }
        }
    }

}