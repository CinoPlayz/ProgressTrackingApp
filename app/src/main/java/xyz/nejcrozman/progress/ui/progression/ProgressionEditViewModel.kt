package xyz.nejcrozman.progress.ui.progression

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.ProgressionDetails
import xyz.nejcrozman.progress.shared.ProgressionDialogAndNumberUiState
import xyz.nejcrozman.progress.shared.mutableStateIn
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository
import xyz.nejcrozman.progress.shared.toProgression
import xyz.nejcrozman.progress.shared.toProgressionDetails
import xyz.nejcrozman.progress.ui.types.TypeDetailsViewModel

class ProgressionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val progressionRepository: ProgressionRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[Destinations.TypesEdit.itemIdArg])

    /**
     * Holds current type ui state
     */
    val uiState: MutableStateFlow<ProgressionDialogAndNumberUiState> =
        progressionRepository.getProgression(itemId)
            .filterNotNull()
            .map {
                ProgressionDialogAndNumberUiState(progressionDetails = it.toProgressionDetails(), valueString = it.value.toString())
            }.mutableStateIn(
                scope = viewModelScope,
                timeout = TypeDetailsViewModel.TIMEOUT_MILLIS,
                initialValue = ProgressionDialogAndNumberUiState()
            )


    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(progressionDetails: ProgressionDetails) {
        uiState.update {
            uiState.value.copy(
                progressionDetails = progressionDetails,
                isEntryValid = validateInput(progressionDetails)
            )
        }

    }

    private fun validateInput(uiState1: ProgressionDetails = uiState.value.progressionDetails): Boolean {
        return with(uiState1) {
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

    suspend fun updateProgress() {
        if (validateInput()) {
            viewModelScope.launch(Dispatchers.IO) {
                progressionRepository.update(uiState.value.progressionDetails.toProgression())
            }
        }
    }

    fun updateUiStateOnlyValueString(valueString: String) {
        uiState.update {
            uiState.value.copy(
                isEntryValid = validateValueString(valueString),
                valueString = valueString
            )
        }
    }

    fun updateUiStateOnlyDialog(boolean: Boolean) {
        uiState.update {
            uiState.value.copy(openDateDialog = boolean)
        }
    }
}