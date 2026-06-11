package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VinylEntity::class, TrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vinylDao(): VinylDao
    abstract fun trackDao(): TrackDao
}
