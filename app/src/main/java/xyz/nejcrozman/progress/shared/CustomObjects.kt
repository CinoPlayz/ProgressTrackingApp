package xyz.nejcrozman.progress.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import xyz.nejcrozman.progress.shared.entities.Type


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
    type_id = id,
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
    id = type_id,
    name = name
)

fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    timeout: Long,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        withTimeoutOrNull(timeout){
            this@mutableStateIn.collect(flow)
        }
    }

    return flow
}