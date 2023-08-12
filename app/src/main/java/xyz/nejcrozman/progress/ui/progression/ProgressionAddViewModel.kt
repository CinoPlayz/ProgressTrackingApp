package xyz.nejcrozman.progress.ui.progression

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.ProgressionAddUiState
import xyz.nejcrozman.progress.shared.ProgressionDetails
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository
import xyz.nejcrozman.progress.shared.toProgression

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ProgressionAddViewModel(savedStateHandle: SavedStateHandle,
                              private val progressionRepository: ProgressionRepository) : ViewModel() {

    private val typeId: Int = checkNotNull(savedStateHandle[Destinations.TypesEdit.itemIdArg])
    /**
     * Holds current item ui state
     */
    var progressionUiState by mutableStateOf(ProgressionAddUiState(progressionDetails = ProgressionDetails(FK_type_id = typeId)))
        private set

    /**
     * Updates the [progressionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(progressionDetails: ProgressionDetails) {
        progressionUiState =
            ProgressionAddUiState(progressionDetails = progressionDetails, isEntryValid = validateInput(progressionDetails))
    }

    fun updateUiStateOnlyValueString(valueString: String) {
        progressionUiState =
            ProgressionAddUiState(progressionDetails = progressionUiState.progressionDetails, isEntryValid = valueString.isNotEmpty().and(valueString.toIntOrNull() != null), valueString = valueString)
    }

    fun updateUiStateOnlyDialog(boolean: Boolean) {
        progressionUiState =
            ProgressionAddUiState(progressionDetails = progressionUiState.progressionDetails, isEntryValid = true, valueString = progressionUiState.valueString, openDateDialog = boolean)
    }

    private fun validateInput(uiState: ProgressionDetails = progressionUiState.progressionDetails): Boolean {
        return with(uiState) {
            value > 0
        }
    }

    suspend fun saveType() {
        if (validateInput()) {
            progressionRepository.insert(progressionUiState.progressionDetails.toProgression())
        }
    }

}

