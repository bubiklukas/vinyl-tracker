package cz.cvut.fit.vinyltracker.ui.feature.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.components.SearchField
import cz.cvut.fit.vinyltracker.ui.components.VinylListItem
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.Gold
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import cz.cvut.fit.vinyltracker.ui.theme.SuccessGreen
import cz.cvut.fit.vinyltracker.ui.theme.WarmMuted
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScreen(
    forCollection: Boolean,
    onDismiss: () -> Unit,
    viewModel: AddViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isDone) {
        if (state.isDone) onDismiss()
    }

    AddScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onToggleVinyl = viewModel::onToggleVinyl,
        onDone = { viewModel.onDone(forCollection) },
    )
}

@Composable
private fun AddScreen(
    state: AddScreenState,
    onQueryChange: (String) -> Unit,
    onToggleVinyl: (Vinyl) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.add_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Cream,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                onClick = onDone,
                enabled = !state.isSaving,
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Gold,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.add_done),
                        color = Gold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.isSearchLoading -> Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Gold)
            }

            state.query.isBlank() || state.results.isEmpty() -> Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = CharcoalMuted,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (state.query.isBlank()) stringResource(R.string.add_search_empty_hint)
                               else stringResource(R.string.add_no_results, state.query),
                        color = CharcoalMuted,
                    )
                }
            }

            else -> LazyColumn {
                items(state.results, key = { it.itunesCollectionId ?: it.title }) { vinyl ->
                    val collId = vinyl.itunesCollectionId
                    val existingOwned = collId?.let { state.existingVinyls[it] }
                    val isExisting = existingOwned != null
                    val isSelected = collId != null && state.selectedVinyls.any { it.itunesCollectionId == collId }

                    VinylListItem(
                        vinyl = vinyl,
                        onClick = { if (!isExisting && !state.isSaving) onToggleVinyl(vinyl) },
                        showBadge = false,
                        modifier = if (isExisting) Modifier.alpha(0.38f) else Modifier,
                        trailingContent = {
                            when {
                                isExisting -> Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stringResource(if (existingOwned) R.string.badge_collection else R.string.badge_wishlist),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (existingOwned) Gold else Teal,
                                        modifier = Modifier.padding(end = 8.dp),
                                    )
                                }
                                isSelected -> IconButton(
                                    onClick = { if (!state.isSaving) onToggleVinyl(vinyl) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = stringResource(R.string.cd_vinyl_added),
                                        tint = SuccessGreen,
                                    )
                                }
                                else -> IconButton(
                                    onClick = { if (!state.isSaving) onToggleVinyl(vinyl) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AddCircle,
                                        contentDescription = stringResource(R.string.cd_add_vinyl),
                                        tint = Gold,
                                    )
                                }
                            }
                        },
                    )
                    HorizontalDivider(color = Cream.copy(alpha = 0.05f))
                }
            }
        }
    }
}
