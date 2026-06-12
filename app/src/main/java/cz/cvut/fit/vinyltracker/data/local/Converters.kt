package cz.cvut.fit.vinyltracker.data.local

import androidx.room.TypeConverter
import java.time.LocalDateTime

object Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}
