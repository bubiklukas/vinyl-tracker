package cz.cvut.fit.vinyltracker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import cz.cvut.fit.vinyltracker.ui.feature.collection.CollectionScreen
import cz.cvut.fit.vinyltracker.ui.feature.detail.DetailScreen
import cz.cvut.fit.vinyltracker.ui.feature.wishlist.WishlistScreen

@Composable
fun Navigation(backStack: NavBackStack<NavKey>, innerPadding: PaddingValues) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = Modifier.padding(innerPadding),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<BackStackKey.Collection> {
                CollectionScreen(
                    onVinylClick = { backStack.add(BackStackKey.VinylDetail(it)) },
                )
            }
            entry<BackStackKey.Wishlist> {
                WishlistScreen(
                    onVinylClick = { backStack.add(BackStackKey.VinylDetail(it)) },
                )
            }
            entry<BackStackKey.VinylDetail> { key ->
                DetailScreen(
                    id = key.id,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
