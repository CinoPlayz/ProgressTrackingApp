package xyz.nejcrozman.progress.ui.types

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.shared.entities.Type
import xyz.nejcrozman.progress.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TypeListScreen(
    navController: NavHostController,
    viewModel: TypeListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Types")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.background
            )
        )

    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destinations.TypesAdd.route) },
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


                val typeListUiState = viewModel.typeListUiState.collectAsState()

                Text(
                    text = stringResource(R.string.selectTypeDescription),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp
                )



                if (typeListUiState.value.typeList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noTypeDescription),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                } else {
                    LazyColumn(modifier = Modifier) {
                        items(items = typeListUiState.value.typeList, key = { it.id }) { type ->
                            ProgressType(
                                type = type,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .combinedClickable(
                                        onClick = { println("On click") },
                                        onLongClick = { navController.navigate("${Destinations.TypesDetail.route}/${type.id}") },
                                    )
                            )


                        }
                    }
                }
            }
        })
}

@Composable
private fun ProgressType(
    type: Type, modifier: Modifier = Modifier
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
                    text = type.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 24.sp
                )
            }
        }
    }
}