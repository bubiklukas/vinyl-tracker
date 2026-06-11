package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class VinylWithTracks(
    @Embedded val vinyl: VinylEntity,
    @Relation(parentColumn = "id", entityColumn = "vinylId")
    val tracks: List<TrackEntity>,
) {
    fun toDomain() = vinyl.toDomain(tracks)
}
