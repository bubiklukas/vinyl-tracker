package cz.cvut.fit.vinyltracker.ui.feature.collection

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

enum class CollectionSortField { NAME, ARTIST, OWNED_SINCE }

data class CollectionScreenState(
    val vinyls: List<Vinyl> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showAddSheet: Boolean = false,
    val query: String = "",
    val sortField: CollectionSortField = CollectionSortField.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CollectionViewModel(private val repository: VinylRepository) : ViewModel() {
    private val debounceMs = 300L
    private val _state = MutableStateFlow(CollectionScreenState())
    val state = _state.asStateFlow()

    init {
        val vinylsFlow = _state
            .map { it.query }
            .distinctUntilChanged()
            .debounce { query -> if (query.isBlank()) 0L else debounceMs }
            .flatMapLatest { query ->
                if (query.isBlank()) repository.getCollection()
                else repository.searchCollection(query)
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

    fun onSort(field: CollectionSortField, direction: SortDirection) {
        _state.update { it.copy(sortField = field, sortDirection = direction) }
    }

    fun showAddSheet() = _state.update { it.copy(showAddSheet = true) }
    fun hideAddSheet() = _state.update { it.copy(showAddSheet = false) }

    fun delete(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }
}

private fun List<Vinyl>.sorted(field: CollectionSortField, dir: SortDirection): List<Vinyl> {
    val comparator: Comparator<Vinyl> = when (field) {
        CollectionSortField.NAME -> compareBy { it.title.lowercase() }
        CollectionSortField.ARTIST -> compareBy { it.artist.lowercase() }
        CollectionSortField.OWNED_SINCE -> compareBy(nullsLast()) { it.ownedSince }
    }
    return if (dir == SortDirection.ASCENDING) sortedWith(comparator) else sortedWith(comparator.reversed())
}
