package cz.cvut.fit.vinyltracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.navigation.BackStackKey
import cz.cvut.fit.vinyltracker.ui.navigation.Navigation

private data class BottomTab(
    val key: BackStackKey,
    val labelRes: Int,
    val cdRes: Int,
    val icon: ImageVector,
)

private val bottomTabs = listOf(
    BottomTab(BackStackKey.Collection, R.string.tab_collection, R.string.cd_tab_collection, Icons.Filled.Album),
    BottomTab(BackStackKey.Wishlist, R.string.tab_wishlist, R.string.cd_tab_wishlist, Icons.Filled.Favorite),
)

@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(BackStackKey.Collection)

    val showBottomBar by remember {
        derivedStateOf { (backStack.lastOrNull() as? BackStackKey)?.showBottomBar ?: true }
    }
    val currentTabIndex by remember {
        derivedStateOf {
            bottomTabs.indexOfLast { backStack.contains(it.key) }.coerceAtLeast(0)
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                NavigationBar {
                    bottomTabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = currentTabIndex == index,
                            onClick = {
                                if (currentTabIndex != index) {
                                    backStack.clear()
                                    backStack.add(tab.key)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = stringResource(tab.cdRes),
                                )
                            },
                            label = { Text(stringResource(tab.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Navigation(backStack = backStack, innerPadding = innerPadding)
    }
}
