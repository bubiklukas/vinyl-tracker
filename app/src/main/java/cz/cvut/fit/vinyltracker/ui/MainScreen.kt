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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import cz.cvut.fit.vinyltracker.ui.navigation.BackStackKey
import cz.cvut.fit.vinyltracker.ui.navigation.Navigation

private val tabs = listOf(BackStackKey.Collection, BackStackKey.Wishlist)
private val tabLabels = listOf("Collection", "Wishlist")
private val tabIcons = listOf(Icons.Filled.Album, Icons.Filled.Favorite)

@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(BackStackKey.Collection)

    val showBottomBar by remember {
        derivedStateOf { (backStack.lastOrNull() as? BackStackKey)?.showBottomBar ?: true }
    }
    val currentTabIndex by remember {
        derivedStateOf {
            tabs.indexOfLast { backStack.contains(it) }.coerceAtLeast(0)
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
                    tabs.forEachIndexed { index, key ->
                        NavigationBarItem(
                            selected = currentTabIndex == index,
                            onClick = {
                                if (currentTabIndex != index) {
                                    backStack.clear()
                                    backStack.add(key)
                                }
                            },
                            icon = { Icon(tabIcons[index], contentDescription = tabLabels[index]) },
                            label = { Text(tabLabels[index]) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Navigation(backStack = backStack, innerPadding = innerPadding)
    }
}
