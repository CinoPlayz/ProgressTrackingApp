package xyz.nejcrozman.progress.ui.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.repositories.TypeRepository

class TypeDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    typesRepository: TypeRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[Destinations.TypesDetail.itemIdArg])

    val uiState: StateFlow<TypeDetailsUiState> =
        typesRepository.getType(itemId)
            .filterNotNull()
            .map {
                TypeDetailsUiState(typeDetails = it.toTypeDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TypeDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TypeDetailsScreen
 */
data class TypeDetailsUiState(
    val typeDetails: TypeDetails = TypeDetails()
)