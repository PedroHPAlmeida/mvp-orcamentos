package br.com.budgets

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.budgets.data.Customer
import br.com.budgets.utils.convertMillisToDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun NewBudgetScreen(navController: NavController, initialCustomer: Customer?) {
    var showAddProductOrServiceModal by remember { mutableStateOf(false) }
    var productsAndServices by remember { mutableStateOf(listOf<Triple<String, String, Int>>()) }
    var customer by remember { mutableStateOf(initialCustomer) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 48.dp, horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.customer_plural),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (customer != null) {
            CustomerCard(customer!!, onClick = {
                val customerJson = Json.encodeToString(customer)
                navController.navigate(
                    "initial_registration?isFromNewBudgetScreen=true&customerJson=${
                        Uri.encode(customerJson)
                    }"
                )
            }, onDelete = { customer = null })
        } else {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    navController.navigate("initial_registration?isFromNewBudgetScreen=true")
                }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.add_button))
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            thickness = 2.dp
        )

        Text(
            text = stringResource(R.string.products_and_services),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        productsAndServices.forEach { (name, value, quantity) ->
            ProductServiceItem(
                name = name,
                value = value,
                quantity = quantity,
                onIncreaseQuantity = {
                    productsAndServices = productsAndServices.map {
                        if (it.first == name && it.second == value) {
                            Triple(it.first, it.second, it.third + 1)
                        } else it
                    }
                },
                onDecreaseQuantity = {
                    productsAndServices = productsAndServices.map {
                        if (it.first == name && it.second == value && it.third > 1) {
                            Triple(it.first, it.second, it.third - 1)
                        } else it
                    }
                },
                onDelete = {
                    productsAndServices = productsAndServices.filter {
                        it.first != name || it.second != value
                    }
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(), onClick = { showAddProductOrServiceModal = true }) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.add_button))
        }

        if (showAddProductOrServiceModal) {
            AddProductOrServiceModal(
                onConfirm = { name, value ->
                    productsAndServices = productsAndServices + Triple(name, "R$ $value", 1) // Quantidade inicial = 1
                    showAddProductOrServiceModal = false
                },
                onDismiss = { showAddProductOrServiceModal = false }
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            thickness = 2.dp
        )

        Text(
            text = stringResource(R.string.place_and_date),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.city_hint)) })

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerFieldToModal()

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            thickness = 2.dp
        )

        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.budget_details),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            Text(text = stringResource(R.string.items_quantity))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = productsAndServices.size.toString())
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(text = stringResource(R.string.total))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "R$ ${calculateTotal(productsAndServices)}")
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Serializar os dados do cliente e serviços para enviar à BudgetViewScreen
                val customerJson = customer?.let { Json.encodeToString(it) } ?: ""
                val servicesJson = Json.encodeToString(productsAndServices)
                navController.navigate(
                    "budget_view?customer=$customerJson&services=$servicesJson"
                )
            }
        ) {
            Text(text = stringResource(R.string.generate_budget))
        }
    }
}

@Composable
fun CustomerCard(customer: Customer, onClick: () -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onClick() }
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = customer.name, fontWeight = FontWeight.Bold)
                Text(text = customer.cpfOrCnpj, fontWeight = FontWeight.Bold)
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete() })
        }
    }
}

@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text(stringResource(R.string.select_date)) },
        placeholder = { Text(stringResource(R.string.br_date_format)) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = null)
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            })

    if (showModal) {
        DatePickerModal(onDateSelected = { selectedDate = it }, onDismiss = { showModal = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(
            onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
            Text(stringResource(R.string.ok))
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(text = stringResource(R.string.cancel))
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

fun calculateTotal(productsAndServices: List<Triple<String, String, Int>>): String {
    return productsAndServices.map {
        val unitValue = it.second.replace("R$ ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
        unitValue * it.third // Multiplica pelo número de itens
    }.sum().let { String.format("%.2f", it) }
}