package cz.cvut.fit.vinyltracker.ui.feature.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.vinyltracker.data.remote.ItunesApi
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.domain.usecase.AddToCollectionUseCase
import cz.cvut.fit.vinyltracker.domain.usecase.AddToWishlistUseCase
import kotlinx.coroutines.FlowPreview
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
    val isLoading: Boolean = false,
    val error: String? = null,
    val addedVinylId: Long? = null,
)

@OptIn(FlowPreview::class)
class AddViewModel(
    private val itunesApi: ItunesApi,
    private val addToCollectionUseCase: AddToCollectionUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it.query }
                .distinctUntilChanged()
                .debounce(300L)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _state.update { it.copy(results = emptyList(), isLoading = false) }
                        return@collectLatest
                    }
                    _state.update { it.copy(isLoading = true, error = null) }
                    try {
                        val results = itunesApi.search(query)
                        _state.update { it.copy(results = results, isLoading = false) }
                    } catch (e: Exception) {
                        _state.update { it.copy(error = e.message, isLoading = false) }
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun onAddVinyl(vinyl: Vinyl, forCollection: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = if (forCollection) addToCollectionUseCase(vinyl) else addToWishlistUseCase(vinyl)
            result.fold(
                onSuccess = { _state.update { s -> s.copy(isLoading = false, addedVinylId = vinyl.itunesCollectionId) } },
                onFailure = { e -> _state.update { s -> s.copy(isLoading = false, error = e.message) } },
            )
        }
    }
}
