package cz.cvut.fit.vinyltracker.domain.usecase

import cz.cvut.fit.vinyltracker.data.remote.ItunesApi
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.exception.MissingCollectionIdException
import cz.cvut.fit.vinyltracker.domain.Vinyl

class AddToWishlistUseCase(
    private val repository: VinylRepository,
    private val itunesApi: ItunesApi,
) {
    suspend operator fun invoke(vinyl: Vinyl): Result<Unit> = runCatching {
        val collectionId = vinyl.itunesCollectionId ?: throw MissingCollectionIdException()
        val tracks = itunesApi.getTracks(collectionId)
        repository.save(vinyl.copy(trackList = tracks, owned = false))
    }
}
