package xyz.nejcrozman.progress.ui.types

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class TypeEditViewModel (
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var typeUiState by mutableStateOf(TypeUiState())
        private set

    //private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    private fun validateInput(uiState: TypeDetails = typeUiState.typeDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}
