package br.com.budgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.budgets.ui.theme.BudgetsTheme

class MainActivity : ComponentActivity() {
    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    // TODO: Consider using innerPadding in the future for better layout management
                    NavHost(
                        navController = navController,
                        startDestination = "initial_registration",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(
                            "initial_registration?isFromHomeScreen={isFromHomeScreen}",
                            arguments = listOf(navArgument("isFromHomeScreen") {
                                type = NavType.BoolType
                                defaultValue = false
                            })
                        ) { backStackEntry ->
                            val isFromHomeScreen = backStackEntry.arguments?.getBoolean("isFromHomeScreen") ?: false
                            InitialRegistrationScreen(navController, isFromHomeScreen)
                        }
                        composable("home") { HomeScreen(navController) }
                        composable("new_budget") { NewBudgetScreen() }
                        composable("my_budgets") { MyBudgetsScreen() }
                    }
                }
            }
        }
    }
}