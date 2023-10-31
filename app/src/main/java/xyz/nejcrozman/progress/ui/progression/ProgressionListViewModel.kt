package xyz.nejcrozman.progress.ui.progression

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.ProgressionListUiState
import xyz.nejcrozman.progress.shared.mutableStateIn
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository
import xyz.nejcrozman.progress.ui.types.TypeDetailsViewModel

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ProgressionListViewModel(savedStateHandle: SavedStateHandle,
                               private val progressionRepository: ProgressionRepository) : ViewModel() {

    private val typeId: Int = checkNotNull(savedStateHandle[Destinations.ProgressionList.itemIdArg])
    /**
     * Holds home ui state. The list of items are retrieved from [ProgressionRepository] and mapped to
     * [progressionListUiState]
     */
    val progressionListUiState: MutableStateFlow<ProgressionListUiState> =
        progressionRepository.getProgressionByTypeIdDESC(typeId).map {ProgressionListUiState(it, typeId) }
            .mutableStateIn(
                scope = viewModelScope,
                timeout = TypeDetailsViewModel.TIMEOUT_MILLIS,
                initialValue = ProgressionListUiState()
            )


    fun updateUiStateDialog(boolean: Boolean){
        progressionListUiState.update { progressionListUiState.value.copy(openDeleteDialog = boolean) }
    }

    fun updateStatusUpdateData(){
        progressionListUiState.update { progressionListUiState.value.copy(updateData = false) }
    }

    fun updateUiProgressionId(id: Int){
        progressionListUiState.update { progressionListUiState.value.copy(progressionId = id) }
    }


    fun deleteType(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            progressionRepository.deleteById(progressionListUiState.value.progressionId)
        }
    }

}
