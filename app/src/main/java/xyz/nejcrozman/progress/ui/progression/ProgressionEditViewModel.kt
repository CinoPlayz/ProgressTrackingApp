package xyz.nejcrozman.progress.ui.progression

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.ProgressionDetails
import xyz.nejcrozman.progress.shared.ProgressionUiState
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
    val uiState: MutableStateFlow<ProgressionUiState> =
        progressionRepository.getProgression(itemId)
            .filterNotNull()
            .map {
                ProgressionUiState(progressionDetails = it.toProgressionDetails())
            }.mutableStateIn(
                scope = viewModelScope,
                timeout = TypeDetailsViewModel.TIMEOUT_MILLIS,
                initialValue = ProgressionUiState()
            )


    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(progressionDetails: ProgressionDetails) {
        uiState.update {
            ProgressionUiState(
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

    suspend fun updateProgress() {
        if (validateInput()) {
            progressionRepository.update(uiState.value.progressionDetails.toProgression())
        }
    }
}