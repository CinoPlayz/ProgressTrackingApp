package xyz.nejcrozman.progress.ui.progression

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.shared.Converters
import xyz.nejcrozman.progress.shared.toProgression
import xyz.nejcrozman.progress.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionEditScreen(
    navController: NavHostController,
    viewModel: ProgressionEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val calenderState =
        rememberUseCaseState(visible = false, embedded = false)

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Edit Progression")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        )

    },
        content = { paddingScaffold ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingScaffold),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(R.string.labelID) + uiState.value.progressionDetails.id.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )

                OutlinedTextField(
                    value = uiState.value.valueString,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Value") },
                    placeholder = { Text(text = "Value to be added to previous") },
                    singleLine = true,
                    onValueChange = {
                        viewModel.updateUiStateOnlyValueString(it)
                    }
                )


                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    onClick = {
                        viewModel.updateUiStateOnlyDialog(true)
                    }) {
                    Text(text = "DATE")
                }

                if (uiState.value.openDateDialog) {
                    calenderState.show()
                    viewModel.updateUiStateOnlyDialog(false)
                }


                CalendarDialog(
                    state = calenderState,
                    selection = CalendarSelection.Date(
                        selectedDate = Converters.dateTimeToDate(uiState.value.progressionDetails.dateOfProgress)
                    ) { newDate ->

                        viewModel.updateUiState(
                            uiState.value.progressionDetails.copy(
                                dateOfProgress = Converters.dateToDateTime(newDate)
                            )
                        )

                    },
                )



                Text(text = "Selected date: ${uiState.value.progressionDetails.toProgression().getDOPFormatted}",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp)

                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    enabled = uiState.value.isEntryValid,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateUiState(
                                uiState.value.progressionDetails.copy(
                                    value =   uiState.value.valueString.toIntOrNull() ?: 1
                                )
                            )

                            viewModel.updateProgress()
                            navController.popBackStack(route = "${Destinations.ProgressionList.route}/${uiState.value.progressionDetails.FK_type_id}", inclusive = true)
                            navController.navigate("${Destinations.ProgressionList.route}/${uiState.value.progressionDetails.FK_type_id}")
                        }
                    }) {
                    Text(text = "UPDATE")
                }
            }
        })
}