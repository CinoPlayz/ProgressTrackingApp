package xyz.nejcrozman.progress.ui.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.TypeDetails
import xyz.nejcrozman.progress.shared.TypeUiState
import xyz.nejcrozman.progress.shared.mutableStateIn
import xyz.nejcrozman.progress.shared.repositories.TypeRepository
import xyz.nejcrozman.progress.shared.toType
import xyz.nejcrozman.progress.shared.toTypeDetails

class TypeEditViewModel (
    savedStateHandle: SavedStateHandle,
    private val typeRepository: TypeRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[Destinations.TypesEdit.itemIdArg])
    /**
     * Holds current type ui state
     */
    val uiState: MutableStateFlow<TypeUiState> =
        typeRepository.getType(itemId)
            .filterNotNull()
            .map {
                TypeUiState(typeDetails = it.toTypeDetails())
            }.mutableStateIn(
                scope = viewModelScope,
                timeout = TypeDetailsViewModel.TIMEOUT_MILLIS,
                initialValue = TypeUiState()
            )


    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(typeDetails: TypeDetails) {
        uiState.update { TypeUiState(typeDetails = typeDetails, isEntryValid = validateInput(typeDetails)) }

    }

    private fun validateInput(uiState1: TypeDetails = uiState.value.typeDetails): Boolean {
        return with(uiState1) {
            name.isNotBlank()
        }
    }

    suspend fun updateType() {
        if (validateInput()) {
            typeRepository.update(uiState.value.typeDetails.toType())
        }
    }
}





