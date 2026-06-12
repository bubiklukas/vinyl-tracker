package cz.cvut.fit.vinyltracker.ui.feature.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.ButtonDelete
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.DetailLoadingWrapper
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.OwnedSinceText
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.TopBar
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.Tracklist
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.VinylHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CollectionDetailScreen(
    id: Long,
    onBackClick: () -> Unit,
    viewModel: CollectionDetailViewModel = koinViewModel(parameters = { parametersOf(id) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isDeleted) { if (state.isDeleted) onBackClick() }

    DetailLoadingWrapper(
        state = state,
        scrollOffset = scrollState.value,
        glowBackground = true
    ) { vinyl ->
        CollectionDetailScreen(
            vinyl = vinyl,
            onBackClick = onBackClick,
            onDelete = viewModel::delete,
            scrollState = scrollState,
        )
    }
}

@Composable
private fun CollectionDetailScreen(
    vinyl: Vinyl,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp),
    ) {
        TopBar(onBackClick)

        VinylHeader(vinyl)

        Spacer(Modifier.height(24.dp))
        OwnedSinceText(vinyl.ownedSince)

        Spacer(Modifier.height(8.dp))
        Tracklist(vinyl.trackList)

        Spacer(Modifier.height(28.dp))
        ButtonDelete(stringResource(R.string.detail_remove_from_collection), onDelete)
    }
}
