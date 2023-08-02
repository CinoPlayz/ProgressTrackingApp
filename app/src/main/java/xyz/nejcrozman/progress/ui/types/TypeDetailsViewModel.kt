package xyz.nejcrozman.progress.ui.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class TypeDetailsViewModel (
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //private val typeId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TypeDetailsScreen
 */
data class TypeDetailsUiState(
    val outOfStock: Boolean = true,
    val typeDetails: TypeDetails = TypeDetails()
)