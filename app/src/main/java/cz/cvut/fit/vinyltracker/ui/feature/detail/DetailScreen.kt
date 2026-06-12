package cz.cvut.fit.vinyltracker.ui.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    id: Long,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(id) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetailScreen(
        state = state,
        onBackClick = onBackClick,
    )
}

@Composable
private fun DetailScreen(
    state: DetailScreenState,
    onBackClick: () -> Unit,
) {
}
