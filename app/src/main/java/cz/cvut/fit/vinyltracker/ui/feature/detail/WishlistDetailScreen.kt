package cz.cvut.fit.vinyltracker.ui.feature.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.MoveToCollectionSection
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.TopBar
import cz.cvut.fit.vinyltracker.ui.feature.detail.components.VinylHeader
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.Gold
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WishlistDetailScreen(
    id: Long,
    onBackClick: () -> Unit,
    onNavigateToCollection: (Long) -> Unit,
    viewModel: WishlistDetailViewModel = koinViewModel(parameters = { parametersOf(id) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isDeleted) { if (state.isDeleted) onBackClick() }
    LaunchedEffect(state.moveDialogState) {
        if (state.moveDialogState == MoveDialogState.NAVIGATE_TO_COLLECTION) {
            onNavigateToCollection(id)
        }
    }

    when (state.moveDialogState) {
        MoveDialogState.CONFIRM -> MoveConfirmDialog(
            vinylTitle = state.vinyl?.title.orEmpty(),
            onConfirm = viewModel::confirmMove,
            onDismiss = viewModel::dismissMoveConfirm,
        )
        MoveDialogState.CONGRATULATIONS -> CongratulationsDialog(
            vinylTitle = state.vinyl?.title.orEmpty(),
            onDismiss = viewModel::dismissCongratulations,
        )
        MoveDialogState.NONE, MoveDialogState.NAVIGATE_TO_COLLECTION -> Unit
    }

    DetailLoadingWrapper(
        state = state,
        scrollOffset = scrollState.value,
        glowBackground = false
    ) { vinyl ->
        WishlistDetailScreen(
            vinyl = vinyl,
            onBackClick = onBackClick,
            onMoveClick = viewModel::onMoveClick,
            onDelete = viewModel::delete,
            scrollState = scrollState,
        )
    }
}

@Composable
private fun WishlistDetailScreen(
    vinyl: Vinyl,
    onBackClick: () -> Unit,
    onMoveClick: () -> Unit,
    onDelete: () -> Unit,
    scrollState: ScrollState,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            TopBar(onBackClick)
            VinylHeader(vinyl)
        }

        Column(modifier = Modifier.padding(vertical = 24.dp)) {
            MoveToCollectionSection(onMoveClick)
            ButtonDelete(stringResource(R.string.detail_remove_from_wishlist), onDelete)
        }
    }
}

@Composable
private fun MoveConfirmDialog(
    vinylTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.detail_move_confirm_title),
                style = MaterialTheme.typography.titleMedium,
                color = Cream,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.detail_move_confirm_text, vinylTitle),
                color = Cream,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.detail_move_confirm_yes), color = Teal)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.detail_move_confirm_no), color = Cream)
            }
        },
    )
}

@Composable
private fun CongratulationsDialog(
    vinylTitle: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.detail_congratulations_title),
                style = MaterialTheme.typography.titleMedium,
                color = Gold,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.detail_congratulations_text, vinylTitle),
                color = Cream,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.detail_congratulations_button), color = Teal)
            }
        },
    )
}
