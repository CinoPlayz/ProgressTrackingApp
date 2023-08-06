package xyz.nejcrozman.progress.ui.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.ui.AppViewModelProvider

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TypeDetailScreen(
    navController: NavHostController,
    viewModel: TypeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Type Details")
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
                val elementId = uiState.value.typeDetails.id
                val elementName = uiState.value.typeDetails.name
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                top = 10.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 0.dp
                            )
                        ) {
                            Text(text = stringResource(R.string.labelID), fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = elementId.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }

                        Row(
                            modifier = Modifier.padding(
                                top = 10.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(text = stringResource(R.string.labelName), fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = elementName, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                }

                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 10.dp)),
                    onClick = { navController.navigate("${Destinations.TypesEdit.route}/${elementId}") }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(text = "EDIT")
                    }

                }

                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 10.dp)),
                    onClick = { viewModel.updateUiStateDialog(true) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(text = "DELETE")
                    }

                }
                
                
                if(uiState.value.openDialog){
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
                                    navController.popBackStack()
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