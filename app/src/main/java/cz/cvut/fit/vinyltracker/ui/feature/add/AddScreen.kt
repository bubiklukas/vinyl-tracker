package cz.cvut.fit.vinyltracker.ui.feature.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.vinyltracker.domain.Vinyl
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScreen(
    forCollection: Boolean,
    onDismiss: () -> Unit,
    viewModel: AddViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddScreen(
        state = state,
        forCollection = forCollection,
        onDismiss = onDismiss,
        onQueryChange = viewModel::onQueryChange,
        onAddVinyl = { vinyl -> viewModel.onAddVinyl(vinyl, forCollection) },
    )
}

@Composable
private fun AddScreen(
    state: AddScreenState,
    forCollection: Boolean,
    onDismiss: () -> Unit,
    onQueryChange: (String) -> Unit,
    onAddVinyl: (Vinyl) -> Unit,
) {
}
