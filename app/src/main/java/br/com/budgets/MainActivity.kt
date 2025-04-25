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
import br.com.budgets.data.Customer
import br.com.budgets.data.OwnerDataStore
import br.com.budgets.ui.theme.BudgetsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verificar se há dados do dono salvos no DataStore
        val hasOwnerData = runBlocking(Dispatchers.IO) {
            val owner = OwnerDataStore.getOwnerData(this@MainActivity).first()
            owner != null // Retorna true se os dados do dono existirem
        }

        setContent {
            BudgetsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = if (hasOwnerData) "home" else "initial_registration",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(
                            "initial_registration?isFromHomeScreen={isFromHomeScreen}&isFromNewBudgetScreen={isFromNewBudgetScreen}&customerJson={customerJson}",
                            arguments = listOf(navArgument("isFromHomeScreen") {
                                type = NavType.BoolType
                                defaultValue = false
                            }, navArgument("isFromNewBudgetScreen") {
                                type = NavType.BoolType
                                defaultValue = false
                            }, navArgument("customerJson") {
                                type = NavType.StringType
                                defaultValue = ""
                            })
                        ) { backStackEntry ->
                            val isFromHomeScreen =
                                backStackEntry.arguments?.getBoolean("isFromHomeScreen") == true
                            val isFromNewBudgetScreen =
                                backStackEntry.arguments?.getBoolean("isFromNewBudgetScreen") == true
                            val customerJson = backStackEntry.arguments?.getString("customerJson")
                            var customer: Customer? = null
                            if (customerJson != null && customerJson.isNotEmpty()) {
                                customer = Json.decodeFromString<Customer>(customerJson)
                            }
                            InitialRegistrationScreen(
                                navController = navController,
                                isFromHomeScreen = isFromHomeScreen,
                                isFromNewBudgetScreen = isFromNewBudgetScreen,
                                customer = customer
                            )
                        }
                        composable("home") { HomeScreen(navController) }
                        composable(
                            "new_budget?customerJson={customerJson}", arguments = listOf(
                                navArgument("customerJson") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                })
                        ) { backStackEntry ->
                            val customerJson = backStackEntry.arguments?.getString("customerJson")
                            var customer: Customer? = null
                            if (customerJson != null && customerJson.isNotEmpty()) {
                                customer = Json.decodeFromString<Customer>(customerJson)
                            }
                            NewBudgetScreen(
                                navController = navController,
                                initialCustomer = customer,
                            )
                        }
                        composable("my_budgets") { MyBudgetsScreen(navController) }

                        // Adicionando o destino para BudgetViewScreen
                        composable(
                            "budget_view?customer={customer}&services={services}",
                            arguments = listOf(
                                navArgument("customer") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("services") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val customerJson = backStackEntry.arguments?.getString("customer")
                            val servicesJson = backStackEntry.arguments?.getString("services")

                            val customer = customerJson?.let {
                                Json.decodeFromString<Customer>(it)
                            }

                            val services = servicesJson?.let {
                                Json.decodeFromString<List<Pair<String, String>>>(it)
                            } ?: emptyList()

                            BudgetViewScreen(
                                navController = navController,
                                customer = customer,
                                services = services
                            )
                        }
                    }
                }
            }
        }
    }
}