package cz.cvut.fit.vinyltracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    primary = Gold,
    secondary = Teal,
    background = Background,
    surface = Surface,
    error = ErrorRed,
    onPrimary = Background,
    onSecondary = Background,
    onBackground = Cream,
    onSurface = Cream,
    onError = Cream,
)

@Composable
fun VinylTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content,
    )
}
