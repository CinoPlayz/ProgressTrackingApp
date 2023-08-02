package xyz.nejcrozman.progress.ui.types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import xyz.nejcrozman.progress.shared.entities.Type
import xyz.nejcrozman.progress.shared.repositories.TypeRepository

/**
 * ViewModel to retrieve all items in the Room database.
 */
class TypeListViewModel(typeRepository: TypeRepository) : ViewModel() {
    /**
     * Holds home ui state. The list of items are retrieved from [TypeRepository] and mapped to
     * [TypeListUiState]
     */
    val typeListUiState: StateFlow<TypeListUiState> =
        typeRepository.getAllTypes().map { TypeListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TypeListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class TypeListUiState(val typeList: List<Type> = listOf())