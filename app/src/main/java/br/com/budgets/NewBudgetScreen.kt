package br.com.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBudgetScreen(navController: NavController, customer: Customer?) {
    var showAddProductOrServiceModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.customer_plural),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (customer != null) {
            CustomerItem(customer)
        } else {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    // Navega para a tela de registro inicial e indica que veio da tela de novo orçamento
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
            AddProductOrServiceModal(onConfirm = { name, value ->
                println("Produto ou Serviço Adicionado: Nome = $name, Valor = $value")
                showAddProductOrServiceModal = false
            }, onDismiss = { showAddProductOrServiceModal = false })
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
            Text(text = "0")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(text = stringResource(R.string.total))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "R$ 0,00")
        }

        // To align the button to the end of the screen
        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Navega para a tela de visualização do orçamento
                navController.navigate("budget_view")
            }
        ) {
            Text(text = stringResource(R.string.generate_budget))
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return formatter.format(calendar.time)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
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

@Composable
fun CustomerItem(customer: Customer) {
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
                .clickable {}
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = customer.name, fontWeight = FontWeight.Bold)
                Text(text = customer.cpfOrCnpj, fontWeight = FontWeight.Bold)
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {})
        }
    }
}