package cz.cvut.fit.vinyltracker.ui.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailScreenState(
    val vinyl: Vinyl? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDeleted: Boolean = false,
)

class DetailViewModel(
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

    fun moveToCollection() {
        viewModelScope.launch { repository.moveToCollection(vinylId, java.time.LocalDateTime.now()) }
    }
}
