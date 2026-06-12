package cz.cvut.fit.vinyltracker.ui.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WishlistScreenState(
    val vinyls: List<Vinyl> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showAddSheet: Boolean = false,
    val query: String = "",
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class WishlistViewModel(private val repository: VinylRepository) : ViewModel() {
    private val debounceMs = 300L
    private val _state = MutableStateFlow(WishlistScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it.query }
                .distinctUntilChanged()
                .debounce { query -> if (query.isBlank()) 0L else debounceMs } // no debounce for blank query
                .flatMapLatest { query ->
                    if (query.isBlank()) repository.getWishlist()
                    else repository.searchWishlist(query)
                }
                .collect { vinyls ->
                    _state.update { it.copy(vinyls = vinyls, isLoading = false) }
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun showAddSheet() = _state.update { it.copy(showAddSheet = true) }
    fun hideAddSheet() = _state.update { it.copy(showAddSheet = false) }

    fun delete(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }
}
