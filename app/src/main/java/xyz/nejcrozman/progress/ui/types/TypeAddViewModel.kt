package xyz.nejcrozman.progress.ui.types

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import xyz.nejcrozman.progress.shared.entities.Type
import xyz.nejcrozman.progress.shared.repositories.TypeRepository

/**
 * ViewModel to validate and insert items in the Room database.
 */
class TypeAddViewModel(private val typeRepository: TypeRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var typeUiState by mutableStateOf(TypeUiState())
        private set

    /**
     * Updates the [typeUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(typeDetails: TypeDetails) {
        typeUiState =
            TypeUiState(typeDetails = typeDetails, isEntryValid = validateInput(typeDetails))
    }

    private fun validateInput(uiState: TypeDetails = typeUiState.typeDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    suspend fun saveType() {
        if (validateInput()) {
            typeRepository.insert(typeUiState.typeDetails.toType())
        }
    }

}

data class TypeUiState(
    val typeDetails: TypeDetails = TypeDetails(),
    val isEntryValid: Boolean = false

)

data class TypeDetails(
    val id: Int = 0,
    val name: String = ""
)

/**
 * Extension function to convert [TypeDetails] to [Type].
 * */
fun TypeDetails.toType(): Type = Type(
    id = id,
    name = name
)


/**
 * Extension function to convert [Type] to [TypeUiState]
 */
fun Type.toTypeUiState(isEntryValid: Boolean = false): TypeUiState = TypeUiState(
    typeDetails = this.toTypeDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Type] to [TypeDetails]
 */
fun Type.toTypeDetails(): TypeDetails = TypeDetails(
    id = id,
    name = name
)


