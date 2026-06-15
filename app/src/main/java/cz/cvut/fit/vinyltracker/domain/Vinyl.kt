package cz.cvut.fit.vinyltracker.domain

import java.time.LocalDateTime

data class Vinyl(
    val id: Long = 0,
    val itunesCollectionId: Long? = null,

    val title: String,
    val artist: String,
    val trackList: List<Track>,
    val year: Int? = null,
    val label: String? = null,
    val genre: String? = null,
    val coverUrl: String? = null,

    val owned: Boolean,
    val ownedSince: LocalDateTime? = null
)
