package cz.cvut.fit.vinyltracker.ui.feature.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.components.FilterPillOption
import cz.cvut.fit.vinyltracker.ui.components.FilterPillRow
import cz.cvut.fit.vinyltracker.ui.components.SearchField
import cz.cvut.fit.vinyltracker.ui.components.SortDirection
import cz.cvut.fit.vinyltracker.ui.components.VinylListItem
import cz.cvut.fit.vinyltracker.ui.feature.add.AddScreen
import cz.cvut.fit.vinyltracker.ui.theme.Background
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import cz.cvut.fit.vinyltracker.ui.theme.WarmMuted
import org.koin.androidx.compose.koinViewModel

@Composable
fun WishlistScreen(
    onVinylClick: (Long) -> Unit,
    viewModel: WishlistViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistScreen(
        state = state,
        onVinylClick = onVinylClick,
        onQueryChange = viewModel::onQueryChange,
        onSort = viewModel::onSort,
        onAddClick = viewModel::showAddSheet,
        onDismissSheet = viewModel::hideAddSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishlistScreen(
    state: WishlistScreenState,
    onVinylClick: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onSort: (WishlistSortField, SortDirection) -> Unit,
    onAddClick: () -> Unit,
    onDismissSheet: () -> Unit,
) {
    if (state.showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            AddScreen(forCollection = false, onDismiss = onDismissSheet)
        }
    }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                containerColor = Teal,
                contentColor = Background,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.wishlist_fab_label), fontWeight = FontWeight.Bold) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.wishlist_title),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Cream,
                modifier = Modifier.padding(top = 20.dp, bottom = 4.dp),
            )

            SearchField(
                query = state.query,
                onQueryChange = onQueryChange,
                modifier = Modifier.padding(vertical = 12.dp),
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Teal)
                }
                state.vinyls.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.wishlist_empty), color = WarmMuted)
                }
                else -> {
                    FilterPillRow(
                        options = listOf(
                            FilterPillOption(WishlistSortField.NAME, stringResource(R.string.filter_sort_name)),
                            FilterPillOption(WishlistSortField.ARTIST, stringResource(R.string.filter_sort_artist)),
                        ),
                        selected = state.sortField,
                        direction = state.sortDirection,
                        onSelect = onSort,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    key(state.sortField, state.sortDirection) {
                        LazyColumn(state = rememberLazyListState()) {
                            items(state.vinyls, key = { it.id }) { vinyl ->
                                VinylListItem(
                                    vinyl = vinyl,
                                    onClick = { onVinylClick(vinyl.id) },
                                )
                                HorizontalDivider(color = Cream.copy(alpha = 0.05f))
                            }
                        }
                    }
                }
            }
        }
    }
}
