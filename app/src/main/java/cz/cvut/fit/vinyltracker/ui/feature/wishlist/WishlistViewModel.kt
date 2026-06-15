package cz.cvut.fit.vinyltracker.ui.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.components.SortDirection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class WishlistSortField { NAME, ARTIST }

data class WishlistScreenState(
    val vinyls: List<Vinyl> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showAddSheet: Boolean = false,
    val query: String = "",
    val sortField: WishlistSortField = WishlistSortField.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class WishlistViewModel(private val repository: VinylRepository) : ViewModel() {
    private val debounceMs = 300L
    private val _state = MutableStateFlow(WishlistScreenState())
    val state = _state.asStateFlow()

    init {
        val vinylsFlow = _state
            .map { it.query }
            .distinctUntilChanged()
            .debounce { query -> if (query.isBlank()) 0L else debounceMs }
            .flatMapLatest { query ->
                if (query.isBlank()) repository.getWishlist()
                else repository.searchWishlist(query)
            }

        val sortFlow = _state
            .map { it.sortField to it.sortDirection }
            .distinctUntilChanged()

        viewModelScope.launch {
            combine(vinylsFlow, sortFlow) { vinyls, (field, dir) ->
                vinyls.sorted(field, dir)
            }.collect { sorted ->
                _state.update { it.copy(vinyls = sorted, isLoading = false) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun onSort(field: WishlistSortField, direction: SortDirection) {
        _state.update { it.copy(sortField = field, sortDirection = direction) }
    }

    fun showAddSheet() = _state.update { it.copy(showAddSheet = true) }
    fun hideAddSheet() = _state.update { it.copy(showAddSheet = false) }

    fun delete(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }
}

private fun List<Vinyl>.sorted(field: WishlistSortField, dir: SortDirection): List<Vinyl> {
    val comparator: Comparator<Vinyl> = when (field) {
        WishlistSortField.NAME -> compareBy { it.title.lowercase() }
        WishlistSortField.ARTIST -> compareBy { it.artist.lowercase() }
    }
    return if (dir == SortDirection.ASCENDING) sortedWith(comparator) else sortedWith(comparator.reversed())
}
