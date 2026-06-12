package cz.cvut.fit.vinyltracker.ui.feature.detail

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.BitmapImage
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.domain.Track
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.theme.Background
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.ErrorRed
import cz.cvut.fit.vinyltracker.ui.theme.Gold
import cz.cvut.fit.vinyltracker.ui.theme.SurfaceHigh
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import cz.cvut.fit.vinyltracker.ui.theme.WarmMuted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

// blur constants
private val GlowOrbSize = 420.dp
private val GlowOrbOffsetY = 0.dp
private val GlowBlurRadius = 130.dp
private val GlowAlpha = 0.65f

@Composable
fun DetailScreen(
    id: Long,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(id) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onBackClick()
    }

    DetailScreen(
        state = state,
        onBackClick = onBackClick,
        onDelete = viewModel::delete,
        onMoveToCollection = viewModel::moveToCollection,
    )
}

@Composable
private fun DetailScreen(
    state: DetailScreenState,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    onMoveToCollection: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Gold)
            }
            state.vinyl == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.detail_not_found), color = WarmMuted)
            }
            else -> DetailContent(
                vinyl = state.vinyl,
                onBackClick = onBackClick,
                onDelete = onDelete,
                onMoveToCollection = onMoveToCollection,
            )
        }
    }
}

@Composable
private fun DetailContent(
    vinyl: Vinyl,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    onMoveToCollection: () -> Unit,
) {
    var dominantColor by remember { mutableStateOf(Color.Transparent) }
    val animatedColor by animateColorAsState(
        targetValue = dominantColor,
        animationSpec = tween(durationMillis = 1000),
        label = "dominant_color",
    )

    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        LaunchedEffect(vinyl.coverUrl) {
            val result = context.imageLoader.execute(
                ImageRequest.Builder(context).data(vinyl.coverUrl).build()
            )
            if (result is SuccessResult) {
                val raw = (result.image as? BitmapImage)?.bitmap ?: return@LaunchedEffect
                val bitmap = if (raw.config == Bitmap.Config.HARDWARE) {
                    raw.copy(Bitmap.Config.ARGB_8888, false)
                } else {
                    raw
                }
                val palette = withContext(Dispatchers.Default) { Palette.from(bitmap).generate() }
                val color = palette.getVibrantColor(palette.getDominantColor(0))
                if (color != 0) dominantColor = Color(color)
            }
        }
    }

    val listState = rememberLazyListState()
    val glowScrollOffset by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) -listState.firstVisibleItemScrollOffset else Int.MIN_VALUE / 2
        }
    }

    Box(Modifier.fillMaxSize().background(Background)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && animatedColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(GlowOrbSize)
                    .offset { IntOffset(0, GlowOrbOffsetY.roundToPx() + glowScrollOffset) }
                    .blur(GlowBlurRadius, BlurredEdgeTreatment.Unbounded)
                    .background(animatedColor.copy(alpha = GlowAlpha), CircleShape)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp),
        ) {
            item {
                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Cream,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.detail_back),
                            color = Cream,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    if (!vinyl.owned) {
                        IconButton(onClick = onMoveToCollection) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = stringResource(R.string.cd_move_to_collection),
                                tint = Teal,
                            )
                        }
                    } else {
                        Spacer(Modifier.size(48.dp))
                    }
                }

                // Album cover
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = vinyl.coverUrl,
                        contentDescription = stringResource(R.string.cd_album_cover),
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }

                // Label · Year
                val labelYear = buildString {
                    vinyl.label?.let { append(it.uppercase()) }
                    if (vinyl.label != null && vinyl.year != null) append(" · ")
                    vinyl.year?.let { append(it) }
                }
                if (labelYear.isNotEmpty()) {
                    Text(
                        text = labelYear,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmMuted,
                        letterSpacing = 1.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Title
                Text(
                    text = vinyl.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Cream,
                )

                Spacer(Modifier.height(6.dp))

                // Artist
                Text(
                    text = vinyl.artist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = WarmMuted,
                )

                // Genre chip
                if (vinyl.genre != null) {
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        InfoChip(text = vinyl.genre)
                    }
                }

                if (!vinyl.owned) {
                    Spacer(Modifier.height(24.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.detail_own_question),
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedButton(
                            onClick = onMoveToCollection,
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Teal),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Teal),
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.detail_move_to_collection))
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                if (vinyl.owned && vinyl.ownedSince != null) {
                    val formatted = vinyl.ownedSince.format(
                        java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy")
                    )
                    Text(
                        text = stringResource(R.string.detail_owned_since, formatted),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 20.dp),
                    )
                }

                // Tracklist header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.detail_tracklist),
                        style = MaterialTheme.typography.labelLarge,
                        color = Gold,
                        letterSpacing = 2.sp,
                    )
                }
                HorizontalDivider(color = SurfaceHigh)
            }

            items(vinyl.trackList, key = { it.position }) { track ->
                TrackRow(track)
                HorizontalDivider(color = SurfaceHigh)
            }

            item {
                Spacer(Modifier.height(28.dp))
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(1.dp, ErrorRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_delete_record),
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.detail_delete))
                }
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = SurfaceHigh,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = WarmMuted,
        )
    }
}

@Composable
private fun TrackRow(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = track.position.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = CharcoalMuted,
            modifier = Modifier.width(28.dp),
        )
        Text(
            text = track.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Cream,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (track.durationMs != null) {
            Text(
                text = formatDuration(track.durationMs),
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalMuted,
            )
        }
    }
}

private fun formatDuration(ms: Int): String {
    val totalSeconds = ms / 1000
    return "${totalSeconds / 60}:${(totalSeconds % 60).toString().padStart(2, '0')}"
}
