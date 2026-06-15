package cz.cvut.fit.vinyltracker.ui.feature.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.remote.ItunesApi
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.domain.usecase.AddToCollectionUseCase
import cz.cvut.fit.vinyltracker.domain.usecase.AddToWishlistUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddScreenState(
    val query: String = "",
    val results: List<Vinyl> = emptyList(),
    val isSearchLoading: Boolean = false,
    val error: String? = null,
    val existingVinyls: Map<Long, Boolean> = emptyMap(),
    val toastVisible: Boolean = false,
)

@OptIn(FlowPreview::class)
class AddViewModel(
    private val itunesApi: ItunesApi,
    private val addToCollectionUseCase: AddToCollectionUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val repository: VinylRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddScreenState())
    val state = _state.asStateFlow()

    private var toastJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getAllItunesIds().collect { map ->
                _state.update { it.copy(existingVinyls = map) }
            }
        }
        viewModelScope.launch {
            _state
                .map { it.query }
                .distinctUntilChanged()
                .debounce(300L)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _state.update { it.copy(results = emptyList(), isSearchLoading = false) }
                        return@collectLatest
                    }
                    _state.update { it.copy(isSearchLoading = true, error = null) }
                    try {
                        val results = itunesApi.search(query)
                        _state.update { it.copy(results = results, isSearchLoading = false) }
                    } catch (e: Exception) {
                        _state.update { it.copy(error = e.message, isSearchLoading = false) }
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun onAddVinyl(vinyl: Vinyl, forCollection: Boolean) {
        viewModelScope.launch {
            if (forCollection) addToCollectionUseCase(vinyl) else addToWishlistUseCase(vinyl)
            toastJob?.cancel()
            toastJob = viewModelScope.launch {
                _state.update { it.copy(toastVisible = true) }
                delay(2500)
                _state.update { it.copy(toastVisible = false) }
            }
        }
    }
}
