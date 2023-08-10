package xyz.nejcrozman.progress.ui.progression

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.shared.toProgression
import xyz.nejcrozman.progress.ui.AppViewModelProvider
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionAddScreen(navController: NavHostController, viewModel: ProgressionAddViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Add Progression")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
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
                OutlinedTextField(
                    value = viewModel.progressionUiState.valueString,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Value") },
                    placeholder = { Text(text = "Value to be added to previous") },
                    onValueChange = {
                            viewModel.updateUiStateOnlyValueString(it)
                    },
                )


                var pickedDate: LocalDateTime = viewModel.progressionUiState.progressionDetails.dateOfProgress
                val context = LocalContext.current
                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                pickedDate = LocalDateTime.of(year, month+1, dayOfMonth, 1, 1)
                                viewModel.updateUiState(viewModel.progressionUiState.progressionDetails.copy(dateOfProgress = pickedDate))
                            }, pickedDate.year, pickedDate.monthValue-1, pickedDate.dayOfMonth

                        ).show()
                    }) {
                    Text(text = "DATE")
                }
                
                Text(text = viewModel.progressionUiState.progressionDetails.toProgression().getDOPFormatted)
                
                

                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    enabled = viewModel.progressionUiState.isEntryValid,
                    onClick = {
                        viewModel.updateUiState(viewModel.progressionUiState.progressionDetails.copy(value = viewModel.progressionUiState.valueString.toIntOrNull() ?: 1))
                        coroutineScope.launch {
                            viewModel.saveType()
                            navController.popBackStack()
                        }
                    }) {
                    Text(text = "ADD")
                }
            }
        })
}