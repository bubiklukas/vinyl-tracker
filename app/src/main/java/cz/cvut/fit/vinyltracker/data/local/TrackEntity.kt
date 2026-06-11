package cz.cvut.fit.vinyltracker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cz.cvut.fit.vinyltracker.domain.Track

@Entity(
    tableName = "tracks",
    foreignKeys = [ForeignKey(
        entity = VinylEntity::class,
        parentColumns = ["id"],
        childColumns = ["vinylId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("vinylId")],
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vinylId: Long,
    val position: Int,
    val title: String,
    val durationMs: Int?,
) {
    fun toDomain() = Track(
        position = position,
        title = title,
        durationMs = durationMs,
    )

    companion object {
        fun fromDomain(track: Track, vinylId: Long) = TrackEntity(
            vinylId = vinylId,
            position = track.position,
            title = track.title,
            durationMs = track.durationMs,
        )
    }
}
