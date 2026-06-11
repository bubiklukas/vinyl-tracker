package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.cvut.fit.vinyltracker.domain.Vinyl

@Entity(tableName = "vinyls")
data class VinylEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String,
    val year: Int?,
    val label: String?,
    val genre: String?,
    val coverUrl: String?,
    val owned: Boolean,
) {
    fun toDomain(tracks: List<TrackEntity>) = Vinyl(
        id = id,
        title = title,
        artist = artist,
        trackList = tracks.map { it.toDomain() },
        year = year,
        label = label,
        genre = genre,
        coverUrl = coverUrl,
        owned = owned,
    )

    companion object {
        fun fromDomain(vinyl: Vinyl) = VinylEntity(
            id = vinyl.id,
            title = vinyl.title,
            artist = vinyl.artist,
            year = vinyl.year,
            label = vinyl.label,
            genre = vinyl.genre,
            coverUrl = vinyl.coverUrl,
            owned = vinyl.owned,
        )
    }
}
