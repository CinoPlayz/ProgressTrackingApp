package xyz.nejcrozman.progress.ui.progression

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ProgressionListViewModel(savedStateHandle: SavedStateHandle,
                               progressionRepository: ProgressionRepository) : ViewModel() {

    private val typeId: Int = checkNotNull(savedStateHandle[Destinations.ProgressionList.itemIdArg])
    /**
     * Holds home ui state. The list of items are retrieved from [ProgressionRepository] and mapped to
     * [progressionListUiState]
     */
    val progressionListUiState: StateFlow<ProgressionListUiState> =
        progressionRepository.getProgressionByTypeId(typeId).map { ProgressionListUiState(it, typeId) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProgressionListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class ProgressionListUiState(val progressionList: List<Progression> = listOf(), val typeId: Int = 0)