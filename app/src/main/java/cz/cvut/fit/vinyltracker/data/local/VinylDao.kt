package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface VinylDao {

    @Transaction
    @Query("SELECT * FROM vinyls")
    fun getAll(): Flow<List<VinylWithTracks>>

    @Transaction
    @Query("SELECT * FROM vinyls WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<VinylWithTracks>>

    suspend fun insert(vinyl: VinylEntity): Long

    @Query("UPDATE vinyls SET owned = :owned WHERE id = :id")
    suspend fun updateOwned(id: Long, owned: Boolean)

    @Query("DELETE FROM vinyls WHERE id = :id")
    suspend fun delete(id: Long)
}
