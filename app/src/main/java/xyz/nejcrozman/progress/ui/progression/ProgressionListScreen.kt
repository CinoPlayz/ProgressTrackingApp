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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.model.Point
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.shared.StraightLinechart
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.ui.AppViewModelProvider
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProgressionListScreen(
    navController: NavHostController,
    viewModel: ProgressionListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
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
                    //Graph
                    val progressionList = progressionListUiState.value.progressionList
                    fun con(localDateTime: LocalDateTime): Float{
                        return localDateTime.toEpochSecond(
                            ZoneId.systemDefault().rules.getOffset(
                                Instant.now()
                            )
                        ).toFloat()
                    }

                    fun con(float: Float): LocalDateTime{
                        return LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(float.toLong()),
                        ZoneId.systemDefault())
                    }
                    
                    val data = List(progressionList.size){
                        Point(progressionList[it].dateOfProgress.dayOfMonth.toFloat(),progressionList[it].value.toFloat())
                        
                    }

                    StraightLinechart(pointsData = data)



                    //Display enteries
                    LazyColumn(modifier = Modifier) {
                        items(
                            items = progressionListUiState.value.progressionList,
                            key = { it.progress_id }) { progression ->
                            ProgressionDisplay(
                                progression = progression,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .combinedClickable(
                                        onClick = { println("On click") /*TODO edit date*/ },
                                        onLongClick = { /*TODO Delete enterie*/ /*navController.navigate("${Destinations.TypesDetail.route}/${type.type_id}")*/ },
                                    )
                            )


                        }
                    }
                }
            }
        })
}

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