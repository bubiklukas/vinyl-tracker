package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [VinylEntity::class, TrackEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vinylDao(): VinylDao
    abstract fun trackDao(): TrackDao
}
