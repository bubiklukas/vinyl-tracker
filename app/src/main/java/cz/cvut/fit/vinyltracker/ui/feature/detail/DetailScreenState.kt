package cz.cvut.fit.vinyltracker.ui.feature.detail

import cz.cvut.fit.vinyltracker.domain.Vinyl

enum class MoveDialogState { NONE, CONFIRM, CONGRATULATIONS }

data class DetailScreenState(
    val vinyl: Vinyl? = null,
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false,
    val moveDialogState: MoveDialogState = MoveDialogState.NONE,
)
