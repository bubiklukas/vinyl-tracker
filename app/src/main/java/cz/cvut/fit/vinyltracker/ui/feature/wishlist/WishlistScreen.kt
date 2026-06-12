package cz.cvut.fit.vinyltracker.ui.feature.wishlist

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
fun WishlistScreen(
    onVinylClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: WishlistViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistScreen(
        state = state,
        onVinylClick = onVinylClick,
        onSearchClick = onSearchClick,
        onAddClick = viewModel::showAddSheet,
        onDismissSheet = viewModel::hideAddSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishlistScreen(
    state: WishlistScreenState,
    onVinylClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit,
    onDismissSheet: () -> Unit,
) {
    if (state.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            AddScreen(
                forCollection = false,
                onDismiss = onDismissSheet,
            )
        }
    }
}
