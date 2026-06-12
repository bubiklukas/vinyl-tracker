package cz.cvut.fit.vinyltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import cz.cvut.fit.vinyltracker.ui.feature.collection.CollectionScreen
import cz.cvut.fit.vinyltracker.ui.feature.detail.DetailScreen
import cz.cvut.fit.vinyltracker.ui.feature.search.SearchScreen
import cz.cvut.fit.vinyltracker.ui.feature.wishlist.WishlistScreen

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(BackStackKey.Collection)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<BackStackKey.Collection> {
                CollectionScreen(
                    onVinylClick = { backStack.add(BackStackKey.VinylDetail(it)) },
                    onSearchClick = { backStack.add(BackStackKey.Search) },
                )
            }
            entry<BackStackKey.Wishlist> {
                WishlistScreen(
                    onVinylClick = { backStack.add(BackStackKey.VinylDetail(it)) },
                    onSearchClick = { backStack.add(BackStackKey.Search) },
                )
            }
            entry<BackStackKey.VinylDetail> { key ->
                DetailScreen(
                    id = key.id,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }
            entry<BackStackKey.Search> {
                SearchScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

        },
    )
}
