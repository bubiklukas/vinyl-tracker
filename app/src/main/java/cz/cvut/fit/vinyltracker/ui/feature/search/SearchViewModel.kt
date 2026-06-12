package cz.cvut.fit.vinyltracker.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchScreenState(
    val query: String = "",
    val results: List<Vinyl> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@OptIn(FlowPreview::class)
class SearchViewModel(private val repository: VinylRepository) : ViewModel() {

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it.query }
                .distinctUntilChanged()
                .debounce(300L)
                .flatMapLatest { query ->
                    if (query.isBlank()) flowOf(emptyList())
                    else repository.search(query)
                }
                .collect { results ->
                    _state.update { it.copy(results = results, isLoading = false) }
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, isLoading = query.isNotBlank()) }
    }
}
