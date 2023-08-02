package xyz.nejcrozman.progress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xyz.nejcrozman.progress.ui.theme.ProgressTheme
import xyz.nejcrozman.progress.ui.types.TypesAddScreen
import xyz.nejcrozman.progress.ui.types.TypesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgressTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    ) {
                    val navController = rememberNavController()
                    NavigationAppHost(navController = navController)
                    //TypesScreen()
                }
            }
        }
    }
}

@Composable
fun NavigationAppHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "types" ){
        composable(Destinations.Types.route){ TypesScreen(navController = navController) }
        composable(Destinations.TypesAdd.route){ TypesAddScreen(navController = navController) }
    }

    
}

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProgressTheme {
        Greeting("Android")
    }
}*/