package br.com.budgets.ui.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.budgets.R
import br.com.budgets.data.local.preferences.OwnerDataStore
import br.com.budgets.domain.model.Customer
import br.com.budgets.domain.model.CustomerAddress
import br.com.budgets.domain.model.Owner
import br.com.budgets.domain.model.OwnerAddress
import br.com.budgets.ui.viewmodel.AddressViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun InitialRegistrationScreen(
    navController: NavController,
    isFromHomeScreen: Boolean = false,
    isFromNewBudgetScreen: Boolean = false,
    customer: Customer? = null
) {
    // Estado para os campos do formulário
    var name by remember { mutableStateOf("") }
    var cpfCnpj by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var complement by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val context = LocalContext.current

    // ViewModel para buscar o endereço
    val viewModel = AddressViewModel()
    val address by viewModel.address.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // Carregar os dados do dono do DataStore, se necessário
    LaunchedEffect(isFromHomeScreen) {
        if (isFromHomeScreen) {
            val owner = OwnerDataStore.getOwnerData(context).first()
            owner?.let {
                name = it.name
                cpfCnpj = it.cpfOrCnpj
                phone = it.phone ?: ""
                email = it.email ?: ""
                cep = it.address?.postalCode ?: ""
                addressDetail = it.address?.street ?: ""
                number = it.address?.number ?: ""
                complement = it.address?.complement ?: ""
                neighborhood = it.address?.neighborhood ?: ""
                city = it.address?.city ?: ""
                state = it.address?.state ?: ""
            }
        } else if (isFromNewBudgetScreen && customer != null) {
            // Preencher os campos com os dados do cliente
            name = customer.name
            cpfCnpj = customer.cpfOrCnpj
            cep = customer.address?.postalCode ?: ""
            addressDetail = customer.address?.street ?: ""
            number = customer.address?.number ?: ""
            complement = customer.address?.complement ?: ""
            neighborhood = customer.address?.neighborhood ?: ""
            city = customer.address?.city ?: ""
            state = customer.address?.state ?: ""
        }
    }

    // Atualizar os campos com os dados retornados pela API
    LaunchedEffect(address) {
        address?.let {
            addressDetail = it.logradouro ?: ""
            neighborhood = it.bairro ?: ""
            city = it.localidade ?: ""
            state = it.uf ?: ""
        }
    }

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

            // Campo de CEP com o botão ao lado
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(3f),
                    value = cep,
                    onValueChange = { cep = it },
                    label = { Text(stringResource(R.string.zip_code)) })
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f), // Ajustar o tamanho proporcional ao campo de CEP
                    onClick = {
                        if (cep.isNotEmpty()) {
                            viewModel.fetchAddress(cep) // Buscar o endereço com base no CEP
                        }
                    }) {
                    Text(
                        text = stringResource(R.string.search), fontSize = 12.sp
                    )
                }
            }

            // Mensagem de erro, se existir
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = addressDetail,
                onValueChange = { addressDetail = it },
                label = { Text(stringResource(R.string.address_detail)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = number,
                onValueChange = { number = it },
                label = { Text(stringResource(R.string.house_number)) })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = complement,
                onValueChange = { complement = it },
                label = { Text(stringResource(R.string.complement)) })

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

            // Lógica dos botões de salvar/cancelar permanece como está
            if (isFromHomeScreen || isFromNewBudgetScreen) {
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
                                // Salvar os dados do dono no DataStore
                                val owner = Owner(
                                    name = name,
                                    cpfOrCnpj = cpfCnpj,
                                    phone = phone,
                                    email = email,
                                    address = OwnerAddress(
                                        street = addressDetail,
                                        number = number,
                                        complement = complement,
                                        neighborhood = neighborhood,
                                        city = city,
                                        state = state,
                                        postalCode = cep
                                    )
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        OwnerDataStore.saveOwnerData(context, owner)
                                        println("Dados do dono salvos com sucesso: $owner")
                                        withContext(Dispatchers.Main) {
                                            navController.navigate("home")
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        println("Erro ao salvar os dados do dono: ${e.message}")
                                    }
                                }
                            }
                            if (isFromNewBudgetScreen) {
                                val address = CustomerAddress(
                                    postalCode = cep,
                                    street = addressDetail,
                                    number = number,
                                    complement = complement,
                                    neighborhood = neighborhood,
                                    city = city,
                                    state = state
                                )
                                val customer = Json.encodeToString(
                                    Customer(
                                        name = name,
                                        cpfOrCnpj = cpfCnpj,
                                        phone = phone,
                                        email = email,
                                        address = address
                                    )
                                )
                                navController.navigate(
                                    "new_budget?customerJson=${Uri.encode(customer)}"
                                )
                            }
                        }) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            } else {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        val owner = Owner(
                            name = name,
                            cpfOrCnpj = cpfCnpj,
                            phone = phone,
                            email = email,
                            address = OwnerAddress(
                                street = addressDetail,
                                number = number,
                                complement = complement,
                                neighborhood = neighborhood,
                                city = city,
                                state = state,
                                postalCode = cep
                            )
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                OwnerDataStore.saveOwnerData(context, owner)
                                println("Dados do dono salvos com sucesso: $owner")
                                withContext(Dispatchers.Main) {
                                    navController.navigate("home")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                println("Erro ao salvar os dados do dono: ${e.message}")
                            }
                        }
                    }) {
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