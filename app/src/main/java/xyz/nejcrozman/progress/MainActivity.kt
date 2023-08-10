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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xyz.nejcrozman.progress.ui.progression.ProgressionAddScreen
import xyz.nejcrozman.progress.ui.progression.ProgressionListScreen
import xyz.nejcrozman.progress.ui.theme.ProgressTheme
import xyz.nejcrozman.progress.ui.types.TypeAddScreen
import xyz.nejcrozman.progress.ui.types.TypeDetailScreen
import xyz.nejcrozman.progress.ui.types.TypeEditScreen
import xyz.nejcrozman.progress.ui.types.TypeListScreen

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
        composable(Destinations.Types.route){ TypeListScreen(navController = navController) }
        composable(Destinations.TypesAdd.route){ TypeAddScreen(navController = navController) }

        composable(route = Destinations.TypesDetail.routeWithArgs,
            arguments = listOf(navArgument(Destinations.TypesDetail.itemIdArg) {
                type = NavType.IntType
            })){
                TypeDetailScreen(navController = navController)
        }

        composable(route = Destinations.TypesEdit.routeWithArgs,
            arguments = listOf(navArgument(Destinations.TypesEdit.itemIdArg) {
                type = NavType.IntType
            })){
            TypeEditScreen(navController = navController)
        }


        composable(route = Destinations.ProgressionList.routeWithArgs,
            arguments = listOf(navArgument(Destinations.ProgressionList.itemIdArg) {
                type = NavType.IntType
            })){
            ProgressionListScreen(navController = navController)
        }


        composable(route = Destinations.ProgressionAdd.routeWithArgs,
            arguments = listOf(navArgument(Destinations.ProgressionAdd.itemIdArg) {
                type = NavType.IntType
            })){
            ProgressionAddScreen(navController = navController)
        }
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