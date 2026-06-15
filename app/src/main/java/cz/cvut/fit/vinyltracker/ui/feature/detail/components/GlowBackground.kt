package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil3.BitmapImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val GLOW_COLOR_ALPHA = 0.45f
private const val GLOW_RADIUS_FACTOR = 1.0f
private const val GLOW_CENTER_Y_FACTOR = 0.15f
private const val GLOW_ANIMATION_MS = 1000
private const val GLOW_BLUR_RADIUS = 60

@Composable
fun GlowBackground(coverUrl: String?, scrollState: ScrollState = ScrollState(0)) {
    val context = LocalContext.current
    var dominantColor by remember(coverUrl) { mutableStateOf(Color.Transparent) }
    val animatedColor by animateColorAsState(
        targetValue = dominantColor,
        animationSpec = tween(GLOW_ANIMATION_MS),
        label = "glow",
    )

    LaunchedEffect(coverUrl) {
        if (coverUrl == null) return@LaunchedEffect
        val request = ImageRequest.Builder(context)
            .data(coverUrl)
            .allowHardware(false)
            .build()
        val result = context.imageLoader.execute(request)
        val bitmap = (result as? SuccessResult)?.image
            ?.let { it as? BitmapImage }
            ?.bitmap ?: return@LaunchedEffect
        val palette = withContext(Dispatchers.IO) { Palette.from(bitmap).generate() }
        palette.dominantSwatch?.rgb?.let { rgb ->
            dominantColor = Color(rgb).copy(alpha = GLOW_COLOR_ALPHA)
        }
    }

    val glowModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier.fillMaxSize().blur(GLOW_BLUR_RADIUS.dp, BlurredEdgeTreatment.Unbounded)
    } else {
        Modifier.fillMaxSize()
    }

    Canvas(glowModifier) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(animatedColor, Color.Transparent),
                center = Offset(size.width / 2f, size.height * GLOW_CENTER_Y_FACTOR - scrollState.value),
                radius = size.width * GLOW_RADIUS_FACTOR,
            )
        )
    }
}
