package xyz.nejcrozman.progress.ui.progression

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.mutableStateIn
import xyz.nejcrozman.progress.shared.repositories.ProgressionRepository
import xyz.nejcrozman.progress.ui.types.TypeDetailsViewModel

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ProgressionListViewModel(savedStateHandle: SavedStateHandle,
                               progressionRepository: ProgressionRepository) : ViewModel() {

    private val typeId: Int = checkNotNull(savedStateHandle[Destinations.ProgressionList.itemIdArg])
    internal val multiDataSetChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    /**
     * Holds home ui state. The list of items are retrieved from [ProgressionRepository] and mapped to
     * [progressionListUiState]
     */
    val progressionListUiState: MutableStateFlow<ProgressionListUiState> =
        progressionRepository.getProgressionByTypeId(typeId).map {ProgressionListUiState(it, typeId) }
            .mutableStateIn(
                scope = viewModelScope,
                timeout = TypeDetailsViewModel.TIMEOUT_MILLIS,
                initialValue = ProgressionListUiState()
            )



    fun updateStatusUpdateData(){
        progressionListUiState.update { progressionListUiState.value.copy(updateData = false) }
    }

    fun updateModelProducer(entries: List<FloatEntry>){
        multiDataSetChartEntryModelProducer.setEntries(entries)
    }

}

/**
 * Ui State for HomeScreen
 */
data class ProgressionListUiState(val progressionList: List<Progression> = listOf(), val typeId: Int = 0, val updateData: Boolean = true                                 )