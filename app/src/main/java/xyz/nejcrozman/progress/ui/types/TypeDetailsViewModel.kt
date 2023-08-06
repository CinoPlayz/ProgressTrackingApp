package xyz.nejcrozman.progress.ui.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.TypeDetails
import xyz.nejcrozman.progress.shared.mutableStateIn
import xyz.nejcrozman.progress.shared.repositories.TypeRepository
import xyz.nejcrozman.progress.shared.toTypeDetails

class TypeDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val typeRepository: TypeRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[Destinations.TypesDetail.itemIdArg])

    /**
     * Holds current type ui state
     */
    val uiState: MutableStateFlow<TypeDetailsUiState> =
        typeRepository.getType(itemId)
            .filterNotNull()
            .map {
                TypeDetailsUiState(typeDetails = it.toTypeDetails())
            }.mutableStateIn(
                scope = viewModelScope,
                timeout = TIMEOUT_MILLIS,
                initialValue = TypeDetailsUiState()
            )

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(typeDetails: TypeDetails) {
        uiState.update { TypeDetailsUiState(typeDetails = typeDetails) }
    }

    fun updateUiStateDialog(openDialog: Boolean) {
        uiState.update { TypeDetailsUiState(typeDetails = uiState.value.typeDetails, openDialog = openDialog) }
    }

    fun deleteType(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            typeRepository.deleteById(uiState.value.typeDetails.id)
        }
    }


}

/**
 * UI state for TypeDetailsScreen
 */
data class TypeDetailsUiState(
    val typeDetails: TypeDetails = TypeDetails(),
    val openDialog: Boolean = false
)