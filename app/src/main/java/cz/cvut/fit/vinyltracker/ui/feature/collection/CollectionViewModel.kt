package cz.cvut.fit.vinyltracker.ui.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CollectionScreenState(
    val vinyls: List<Vinyl> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showAddSheet: Boolean = false,
)

class CollectionViewModel(private val repository: VinylRepository) : ViewModel() {

    private val _state = MutableStateFlow(CollectionScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCollection().collect { vinyls ->
                _state.update { it.copy(vinyls = vinyls, isLoading = false) }
            }
        }
    }

    fun showAddSheet() = _state.update { it.copy(showAddSheet = true) }
    fun hideAddSheet() = _state.update { it.copy(showAddSheet = false) }

    fun delete(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }
}
