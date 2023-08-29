package xyz.nejcrozman.progress.ui.progression

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.patrykandpatrick.vico.compose.axis.axisLineComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.shared.Converters
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.recreateRoute
import xyz.nejcrozman.progress.shared.rememberMarker
import xyz.nejcrozman.progress.ui.AppViewModelProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProgressionListScreen(
    navController: NavHostController,
    viewModel: ProgressionListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.progressionListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Progress")
            },
            colors = topAppBarColors(
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("${Destinations.ProgressionAdd.route}/${viewModel.progressionListUiState.value.typeId}") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.typeAddTitle)
                )
            }
        },
        content = { paddingScaffold ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.verticalScroll(rememberScrollState())
                    .padding(paddingScaffold),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {


                val progressionListUiState = viewModel.progressionListUiState.collectAsState()

                Text(
                    text = stringResource(R.string.labelEntries),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp
                )



                if (progressionListUiState.value.progressionList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noEntriesDescription),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                } else {

                    //Display entries
                    LazyColumn(modifier = Modifier
                        ) {
                        //Header
                        item {
                            //Chart
                            val min = progressionListUiState.value.progressionList.minOf { Converters.dateTimeToDate(it.dateOfProgress).toEpochDay() }

                            if(progressionListUiState.value.updateData){
                                val uiStateValues = progressionListUiState.value
                                val entries: MutableList<FloatEntry> = mutableListOf()

                                for(progress in uiStateValues.progressionList){
                                    entries.add(FloatEntry(Converters.dateTimeToDate(progress.dateOfProgress).toEpochDay()-min.toFloat(), progress.value.toFloat()))
                                }

                                viewModel.updateModelProducer(entries)
                                viewModel.updateStatusUpdateData()
                            }

                            val bottomAxisValueFormatter =
                                AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
                                    LocalDate.ofEpochDay(min+x.toLong()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                }

                            val marker = rememberMarker()
                            ProvideChartStyle(m3ChartStyle()) {
                                Chart(
                                    chart =  lineChart(persistentMarkers = remember(marker) { mapOf(PERSISTENT_MARKER_X to marker) }),
                                    chartModelProducer = viewModel.multiDataSetChartEntryModelProducer,
                                    startAxis = rememberStartAxis(),
                                    bottomAxis = rememberBottomAxis(valueFormatter = bottomAxisValueFormatter,
                                        axis = axisLineComponent(strokeWidth = 10.dp)
                                    ) ,
                                    marker = marker,
                                    getXStep = ({
                                        it.entries[0].size.toFloat()
                                    })
                                )
                            }
                        }

                        //Body
                        items(
                            items = progressionListUiState.value.progressionList,
                            key = { it.progress_id }) { progression ->
                            ProgressionDisplay(
                                progression = progression,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .combinedClickable(
                                        onClick = { navController.navigate("${Destinations.ProgressionEdit.route}/${progression.progress_id}") },
                                        onLongClick = { viewModel.updateUiStateDialog(true)
                                                      viewModel.updateUiProgressionId(progression.progress_id)},
                                    )
                            )


                        }
                    }
                }


                if(uiState.value.openDeleteDialog){
                    AlertDialog(onDismissRequest = {  viewModel.updateUiStateDialog(false)},
                        title = {
                            Text(text = "Attention")
                        },
                        text = {
                            Text(text = "Are you sure you want to delete?")
                        },

                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.updateUiStateDialog(false)
                                    viewModel.deleteType(coroutineScope)
                                    recreateRoute(navController, "${Destinations.ProgressionList.route}/${uiState.value.typeId}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    viewModel.updateUiStateDialog(false)
                                }) {
                                Text("No")
                            }
                        }

                    )


                }
            }
        })
}


private const val PERSISTENT_MARKER_X = 10f

@Composable
private fun ProgressionDisplay(
    progression: Progression, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ID: ${progression.progress_id}, Date: ${progression.getDOPFormatted}, Value: ${progression.value}",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 24.sp
                )
            }
        }
    }
}
