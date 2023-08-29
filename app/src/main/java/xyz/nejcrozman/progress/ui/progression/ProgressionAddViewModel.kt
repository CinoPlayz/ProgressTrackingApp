package xyz.nejcrozman.progress.ui.progression

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.ProgressionDialogAndNumberUiState
import xyz.nejcrozman.progress.shared.ProgressionDetails
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository
import xyz.nejcrozman.progress.shared.toProgression

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ProgressionAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val progressionRepository: ProgressionRepository
) : ViewModel() {

    private val typeId: Int = checkNotNull(savedStateHandle[Destinations.TypesEdit.itemIdArg])

    /**
     * Holds current item ui state
     */
    var progressionUiState by mutableStateOf(
        ProgressionDialogAndNumberUiState(
            progressionDetails = ProgressionDetails(
                FK_type_id = typeId
            )
        )
    )
        private set

    /**
     * Updates the [progressionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(progressionDetails: ProgressionDetails) {
        progressionUiState =
            progressionUiState.copy(
                progressionDetails = progressionDetails,
                isEntryValid = validateInput(progressionDetails)
            )
    }

    fun updateUiStateOnlyValueString(valueString: String) {
        progressionUiState = progressionUiState.copy(
            isEntryValid = validateValueString(valueString),
            valueString = valueString
        )
    }

    fun updateUiStateOnlyDialog(boolean: Boolean) {
        progressionUiState = progressionUiState.copy(openDateDialog = boolean)
    }

    private fun validateInput(uiState: ProgressionDetails = progressionUiState.progressionDetails): Boolean {
        return with(uiState) {
            value >= -1
        }
    }

    private fun validateValueString(valueString: String): Boolean{
        if(valueString.isNotEmpty() && valueString.toIntOrNull() != null){
            if(valueString.toInt() >= -1){
                return true
            }
            return false
        }
        return false
    }

    suspend fun saveType() {
        if (validateInput()) {
            viewModelScope.launch(Dispatchers.IO) {
                val previousValue: Int = progressionRepository.getProgressionPreviousValue(
                    progressionUiState.progressionDetails.FK_type_id,
                    progressionUiState.progressionDetails.dateOfProgress
                )
                val updateProgressDetails: ProgressionDetails =
                    progressionUiState.progressionDetails.copy(value = progressionUiState.progressionDetails.value + previousValue)
                progressionRepository.insert(updateProgressDetails.toProgression())
            }

        }
    }

}

