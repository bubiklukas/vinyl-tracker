package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    suspend fun insertAll(tracks: List<TrackEntity>)

}
