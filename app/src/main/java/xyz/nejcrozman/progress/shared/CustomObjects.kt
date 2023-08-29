package xyz.nejcrozman.progress.shared

import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.entities.Type
import java.time.LocalDateTime


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

/*
/**
 * Extension function to convert [Ty] to [TypeUiState]
 */
fun Type.toTypeUiState(isEntryValid: Boolean = false): TypeUiState = TypeUiState(
    typeDetails = this.toTypeDetails(),
    isEntryValid = isEntryValid
)*/

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
        withTimeoutOrNull(timeout) {
            this@mutableStateIn.collect(flow)
        }
    }

    return flow
}

fun recreateRoute(navController: NavHostController, route: String, inclusive: Boolean = true){
    navController.popBackStack(route = route, inclusive = inclusive)
    navController.navigate(route = route)
}


/**
 * Ui State for ProgressionList
 */
data class ProgressionListUiState(
    val progressionList: List<Progression> = listOf(),
    val typeId: Int = 0,
    val updateData: Boolean = true,
    val openDeleteDialog: Boolean = false,
    val progressionId: Int = 0
)


data class ProgressionDialogAndNumberUiState(
    val progressionDetails: ProgressionDetails = ProgressionDetails(),
    val isEntryValid: Boolean = true,
    val valueString: String = "1",
    val openDateDialog: Boolean = false
)


data class ProgressionDetails(
    val id: Int = 0,
    val FK_type_id: Int = 0,
    val value: Int = 1,
    val dateOfProgress: LocalDateTime = LocalDateTime.now(),
    val dateOfLastEdit: LocalDateTime = LocalDateTime.now()
)

/**
 * Extension function to convert [ProgressionDetails] to [Progression].
 * */
fun ProgressionDetails.toProgression(): Progression = Progression(
    progress_id = id,
    FK_type_id = FK_type_id,
    value = value,
    dateOfProgress = dateOfProgress,
    dateOfLastEdit = dateOfLastEdit
)

fun Progression.toProgressionDetails(): ProgressionDetails = ProgressionDetails(
    id = progress_id,
    FK_type_id = FK_type_id,
    value = value,
    dateOfProgress = dateOfProgress,
    dateOfLastEdit = dateOfLastEdit
)



