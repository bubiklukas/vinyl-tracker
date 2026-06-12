package cz.cvut.fit.vinyltracker.ui.feature.collection

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.feature.add.AddScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun CollectionScreen(
    onVinylClick: (Long) -> Unit,
    viewModel: CollectionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectionScreen(
        state = state,
        onVinylClick = onVinylClick,
        onQueryChange = viewModel::onQueryChange,
        onAddClick = viewModel::showAddSheet,
        onDismissSheet = viewModel::hideAddSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionScreen(
    state: CollectionScreenState,
    onVinylClick: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDismissSheet: () -> Unit,
) {
    if (state.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            AddScreen(
                forCollection = true,
                onDismiss = onDismissSheet,
            )
        }
    }
}
