package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import cz.cvut.fit.vinyltracker.domain.Vinyl
import java.time.LocalDateTime

@Entity(
    tableName = "vinyls",
    indices = [Index(value = ["itunesCollectionId"], unique = true)],
)
data class VinylEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itunesCollectionId: Long?,
    val title: String,
    val artist: String,
    val year: Int?,
    val label: String?,
    val genre: String?,
    val coverUrl: String?,
    val owned: Boolean,
    val ownedSince: LocalDateTime? = null,
) {
    fun toDomain(tracks: List<TrackEntity>) = Vinyl(
        id = id,
        itunesCollectionId = itunesCollectionId,
        title = title,
        artist = artist,
        trackList = tracks.map { it.toDomain() },
        year = year,
        label = label,
        genre = genre,
        coverUrl = coverUrl,
        owned = owned,
        ownedSince = ownedSince,
    )

    companion object {
        fun fromDomain(vinyl: Vinyl) = VinylEntity(
            id = vinyl.id,
            itunesCollectionId = vinyl.itunesCollectionId,
            title = vinyl.title,
            artist = vinyl.artist,
            year = vinyl.year,
            label = vinyl.label,
            genre = vinyl.genre,
            coverUrl = vinyl.coverUrl,
            owned = vinyl.owned,
            ownedSince = vinyl.ownedSince,
        )
    }
}
