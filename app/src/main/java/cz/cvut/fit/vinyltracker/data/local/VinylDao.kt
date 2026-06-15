package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class ItunesIdOwned(val itunesCollectionId: Long, val owned: Boolean)

@Dao
interface VinylDao {

    @Transaction
    @Query("SELECT * FROM vinyls WHERE owned = 1")
    fun getCollection(): Flow<List<VinylWithTracks>>

    @Transaction
    @Query("SELECT * FROM vinyls WHERE owned = 0")
    fun getWishlist(): Flow<List<VinylWithTracks>>

    @Transaction
    @Query("SELECT * FROM vinyls WHERE id = :id")
    fun getById(id: Long): Flow<VinylWithTracks?>

    @Transaction
    @Query("SELECT * FROM vinyls")
    fun getAll(): Flow<List<VinylWithTracks>>

    @Query("SELECT itunesCollectionId, owned FROM vinyls WHERE itunesCollectionId IS NOT NULL")
    fun getAllItunesIds(): Flow<List<ItunesIdOwned>>

    @Transaction
    @Query("SELECT * FROM vinyls WHERE owned = 1 AND (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%')")
    fun searchCollection(query: String): Flow<List<VinylWithTracks>>

    @Transaction
    @Query("SELECT * FROM vinyls WHERE owned = 0 AND (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%')")
    fun searchWishlist(query: String): Flow<List<VinylWithTracks>>

    @Insert
    suspend fun insert(vinyl: VinylEntity): Long

    @Query("UPDATE vinyls SET owned = 1, ownedSince = :ownedSince WHERE id = :id")
    suspend fun moveToCollection(id: Long, ownedSince: String?)

    @Query("DELETE FROM vinyls WHERE id = :id")
    suspend fun delete(id: Long)
}
