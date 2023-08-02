package xyz.nejcrozman.progress.ui.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import xyz.nejcrozman.progress.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypesScreen(navController: NavHostController) {
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
        content = { paddingScaffold ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingScaffold),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {
                //Greeting("Android")
                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    onClick = { navController.navigate(Destinations.TypesAdd.route) }) {
                    Text(text = "ADD TYPE")
                }
            }
        })
}