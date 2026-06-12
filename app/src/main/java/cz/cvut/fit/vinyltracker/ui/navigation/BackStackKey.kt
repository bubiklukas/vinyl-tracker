package cz.cvut.fit.vinyltracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class BackStackKey : NavKey {
    open val showBottomBar: Boolean = true

    @Serializable data object Collection : BackStackKey()
    @Serializable data object Wishlist : BackStackKey()
    @Serializable data class VinylDetail(val id: Long) : BackStackKey() {
        override val showBottomBar = false
    }

}
