package cz.cvut.fit.vinyltracker.data.repository

import cz.cvut.fit.vinyltracker.data.local.TrackDao
import cz.cvut.fit.vinyltracker.data.local.TrackEntity
import cz.cvut.fit.vinyltracker.data.local.VinylDao
import cz.cvut.fit.vinyltracker.data.local.VinylEntity
import cz.cvut.fit.vinyltracker.data.remote.ItunesApi
import cz.cvut.fit.vinyltracker.domain.Track
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Purely local Vinyl repository */
class VinylRepository(
    private val vinylDao: VinylDao,
    private val trackDao: TrackDao
) {
    fun getAll(): Flow<List<Vinyl>> =
        vinylDao.getAll().map { list -> list.map { it.toDomain() } }

    fun search(query: String): Flow<List<Vinyl>> =
        vinylDao.search(query).map { list -> list.map { it.toDomain() } }

    suspend fun save(vinyl: Vinyl) {
        val id = vinylDao.insert(VinylEntity.fromDomain(vinyl))
        trackDao.insertAll(vinyl.trackList.map { TrackEntity.fromDomain(it, id) })
    }

    suspend fun setOwned(id: Long, owned: Boolean) =
        vinylDao.updateOwned(id, owned)

    suspend fun delete(id: Long) =
        vinylDao.delete(id)
}
