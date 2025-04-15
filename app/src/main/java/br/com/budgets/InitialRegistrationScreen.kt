package br.com.budgets

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.budgets.data.Customer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun InitialRegistrationScreen(
    navController: NavController,
    isFromHomeScreen: Boolean = false,
    isFromNewBudgetScreen: Boolean = false, // Nova variável para identificar a origem
    customer: Customer? = null
) {
    var name by remember { mutableStateOf(customer?.name ?: "")}
    var cpfCnpj by remember { mutableStateOf(customer?.cpfOrCnpj ?: "") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = cpfCnpj,
                onValueChange = { cpfCnpj = it },
                label = { Text(stringResource(R.string.cpf_cnpj)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(R.string.phone)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) })

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.address),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = cep,
                onValueChange = { cep = it },
                label = { Text(stringResource(R.string.zip_code)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = addressDetail,
                onValueChange = { addressDetail = it },
                label = { Text(stringResource(R.string.address_detail)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text(stringResource(R.string.neighborhood)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = city,
                onValueChange = { city = it },
                label = { Text(stringResource(R.string.city)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state,
                onValueChange = { state = it },
                label = { Text(stringResource(R.string.state)) })

            Spacer(modifier = Modifier.height(16.dp))

            if (isFromHomeScreen || isFromNewBudgetScreen) { // Condição ajustada
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f), onClick = { navController.navigateUp() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f), onClick = {
                            if (isFromHomeScreen) {
                                navController.navigate("home")
                            }
                            if (isFromNewBudgetScreen) {
                                val customer = Json.encodeToString(Customer(name, cpfCnpj))
                                navController.navigate(
                                    "new_budget?customerJson=${
                                        Uri.encode(
                                            customer
                                        )
                                    }"
                                )
                            }
                        }) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            } else {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("home") }) {
                    Text(text = stringResource(R.string.save))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        val buttonVisibility by remember {
            derivedStateOf {
                scrollState.value < with(density) { 48.dp.toPx() }
            }
        }

        if (buttonVisibility && !isFromHomeScreen && !isFromNewBudgetScreen) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                onClick = { navController.navigate("home") }) {
                Text(text = stringResource(R.string.skip), fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialRegistrationScreenPreview() {
    InitialRegistrationScreen(navController = rememberNavController())
}