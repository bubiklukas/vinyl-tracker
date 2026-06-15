package cz.cvut.fit.vinyltracker.domain

data class Track(
    val position: Int,
    val title: String,
    val durationMs: Int? = null,
)
