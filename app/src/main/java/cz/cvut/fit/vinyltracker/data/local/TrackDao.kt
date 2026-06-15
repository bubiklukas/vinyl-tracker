package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TrackDao {

    @Insert
    suspend fun insertAll(tracks: List<TrackEntity>)
}
