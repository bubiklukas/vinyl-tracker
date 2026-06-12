package cz.cvut.fit.vinyltracker.ui.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class WishlistDetailViewModel(
    private val vinylId: Long,
    private val repository: VinylRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getById(vinylId).collect { vinyl ->
                _state.update { it.copy(vinyl = vinyl, isLoading = false) }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete(vinylId)
            _state.update { it.copy(isDeleted = true) }
        }
    }

    fun onMoveClick() {
        _state.update { it.copy(moveDialogState = MoveDialogState.CONFIRM) }
    }

    fun dismissMoveConfirm() {
        _state.update { it.copy(moveDialogState = MoveDialogState.NONE) }
    }

    fun confirmMove() {
        viewModelScope.launch {
            repository.moveToCollection(vinylId, LocalDateTime.now())
            _state.update { it.copy(moveDialogState = MoveDialogState.CONGRATULATIONS) }
        }
    }

    fun dismissCongratulations() {
        _state.update { it.copy(moveDialogState = MoveDialogState.NONE, isDeleted = true) }
    }
}
