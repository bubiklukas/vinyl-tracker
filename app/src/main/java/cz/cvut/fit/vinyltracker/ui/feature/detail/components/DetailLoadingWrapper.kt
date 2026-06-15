package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.feature.detail.DetailScreenState
import cz.cvut.fit.vinyltracker.ui.theme.Background
import cz.cvut.fit.vinyltracker.ui.theme.Gold
import cz.cvut.fit.vinyltracker.ui.theme.WarmMuted

@Composable
fun DetailLoadingWrapper(
    state: DetailScreenState,
    scrollState: ScrollState = ScrollState(0),
    glowBackground: Boolean,
    content: @Composable (Vinyl) -> Unit,
) {
    Box(Modifier.fillMaxSize().background(Background)) {
        if (glowBackground) GlowBackground(coverUrl = state.vinyl?.coverUrl, scrollState = scrollState)
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Gold)
            }
            state.vinyl == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.detail_not_found), color = WarmMuted)
            }
            else -> content(state.vinyl)
        }
    }
}
