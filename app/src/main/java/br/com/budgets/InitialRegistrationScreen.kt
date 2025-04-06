package br.com.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun InitialRegistrationScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var cpfCnpj by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { navController.navigate("home") }
            ) {
                Text(text = stringResource(R.string.skip), fontSize = 14.sp)
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cpfCnpj,
            onValueChange = { cpfCnpj = it },
            label = { Text(stringResource(R.string.cpf_cnpj)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.phone)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) }
        )

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
            label = { Text(stringResource(R.string.zip_code)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = addressDetail,
            onValueChange = { addressDetail = it },
            label = { Text(stringResource(R.string.address_detail)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = neighborhood,
            onValueChange = { neighborhood = it },
            label = { Text(stringResource(R.string.neighborhood)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = city,
            onValueChange = { city = it },
            label = { Text(stringResource(R.string.city)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state,
            onValueChange = { state = it },
            label = { Text(stringResource(R.string.state)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("home") }
        ) {
            Text(text = stringResource(R.string.save))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun InitialRegistrationScreenPreview() {
    InitialRegistrationScreen(navController = rememberNavController())
}