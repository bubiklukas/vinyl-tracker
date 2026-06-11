package cz.cvut.fit.vinyltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Track
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.theme.VinylTrackerTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val repository: VinylRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VinylTrackerTheme {
                val album = remember { mutableStateOf<Vinyl?>(null) }
                val tracks = remember { mutableStateOf<List<Track>>(emptyList()) }

                LaunchedEffect(Unit) {
                    val results = repository.search("Paradise Again Swedish House Mafia")
                    val found = results.firstOrNull { it.title.contains("Paradise", ignoreCase = true) }
                    if (found != null) {
                        album.value = found
                        tracks.value = repository.getTracks(found.itunesCollectionId!!)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        album.value?.let { vinyl ->
                            item {
                                Column {
                                    AsyncImage(
                                        model = vinyl.coverUrl,
                                        contentDescription = vinyl.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                                    )
                                    Text("${vinyl.artist} — ${vinyl.title} (${vinyl.year})")
                                }
                            }
                        }
                        items(tracks.value) { track ->
                            Text("${track.position}. ${track.title}")
                        }
                    }
                }
            }
        }
    }
}
