package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Dao

@Dao
interface TrackDao {

    suspend fun insertAll(tracks: List<TrackEntity>)
}
