package cz.cvut.fit.vinyltracker.ui.feature.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import cz.cvut.fit.vinyltracker.ui.theme.SuccessGreen
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScreen(
    forCollection: Boolean,
    onDismiss: () -> Unit,
    viewModel: AddViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddScreen(
        forCollection = forCollection,
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onAddVinyl = { vinyl -> viewModel.onAddVinyl(vinyl, forCollection) },
        onDismiss = onDismiss,
    )
}

@Composable
private fun AddScreen(
    forCollection: Boolean,
    state: AddScreenState,
    onQueryChange: (String) -> Unit,
    onAddVinyl: (Vinyl) -> Unit,
    onDismiss: () -> Unit,
) {
    val toastMessage = stringResource(
        if (forCollection) R.string.add_toast_collection else R.string.add_toast_wishlist
    )

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
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_close),
                    tint = Cream,
                )
            }
        }

        AnimatedVisibility(
            visible = state.toastVisible,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SuccessGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = toastMessage,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
        )

        LazyColumn(contentPadding = PaddingValues(top = 16.dp)) {
            when {
                state.isSearchLoading -> item(key = "loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Gold)
                    }
                }
                state.query.isBlank() || state.results.isEmpty() -> item(key = "empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
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
                }
                else -> items(state.results, key = { it.itunesCollectionId ?: it.title }) { vinyl ->
                    val collId = vinyl.itunesCollectionId
                    val existingOwned = collId?.let { state.existingVinyls[it] }
                    val isExisting = existingOwned != null

                    VinylListItem(
                        vinyl = vinyl,
                        onClick = { if (!isExisting) onAddVinyl(vinyl) },
                        showBadge = false,
                        trailingContent = {
                            when {
                                isExisting -> Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stringResource(if (existingOwned) R.string.badge_collection else R.string.badge_wishlist),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Gold,
                                        modifier = Modifier.padding(end = 8.dp),
                                    )
                                }
                                else -> IconButton(onClick = { onAddVinyl(vinyl) }) {
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
