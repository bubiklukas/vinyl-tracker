package cz.cvut.fit.vinyltracker.data.remote

import cz.cvut.fit.vinyltracker.domain.Track
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.serialization.Serializable

@Serializable
data class ItunesSearchResponse(
    val results: List<ItunesCollectionDto>,
)

@Serializable
data class ItunesCollectionDto(
    val collectionId: Long,
    val artistName: String,
    val collectionName: String,
    val artworkUrl100: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
) {
    fun toVinyl() = Vinyl(
        itunesCollectionId = collectionId,
        title = collectionName,
        artist = artistName,
        trackList = emptyList(),
        year = releaseDate?.take(4)?.toIntOrNull(),
        genre = primaryGenreName,
        coverUrl = artworkUrl100?.replace("100x100", "600x600"),
        owned = false,
    )
}

@Serializable
data class ItunesTrackDto(
    val trackNumber: Int? = null,
    val trackName: String? = null,
    val trackTimeMillis: Int? = null,
) {
    fun toTrack(): Track? {
        return Track(
            position = trackNumber ?: return null,
            title = trackName ?: return null,
            durationMs = trackTimeMillis ?: return null,
        )
    }
}

@Serializable
data class ItunesTracksResponse(
    val results: List<ItunesTrackDto>,
)