package cz.cvut.fit.vinyltracker.ui.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SearchScreen(
        state = state,
        onBackClick = onBackClick,
        onQueryChange = viewModel::onQueryChange,
    )
}

@Composable
private fun SearchScreen(
    state: SearchScreenState,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
) {
}
