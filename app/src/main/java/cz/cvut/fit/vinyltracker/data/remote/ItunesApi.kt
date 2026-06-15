package cz.cvut.fit.vinyltracker.data.remote

import cz.cvut.fit.vinyltracker.domain.Track
import cz.cvut.fit.vinyltracker.domain.Vinyl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ItunesApi(private val client: HttpClient) {

    suspend fun search(query: String): List<Vinyl> {
        val response = client.get("search") {
            parameter("term", query)
            parameter("entity", "album")
            parameter("limit", 25)
        }.body<ItunesSearchResponse>()

        return response.results.map { it.toVinyl() }
    }

    suspend fun getTracks(collectionId: Long): List<Track> {
        val response = client.get("lookup") {
            parameter("id", collectionId)
            parameter("entity", "song")
        }.body<ItunesTracksResponse>()

        // drop first because it is an album
        return response.results.drop(1).mapNotNull { it.toTrack() }
    }
}
