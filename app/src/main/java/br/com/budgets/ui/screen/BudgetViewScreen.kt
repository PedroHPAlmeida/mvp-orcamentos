package br.com.budgets.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.budgets.R
import br.com.budgets.domain.model.Customer
import br.com.budgets.domain.model.CustomerAddress
import br.com.budgets.domain.model.Owner
import br.com.budgets.data.local.preferences.OwnerDataStore
import br.com.budgets.utils.convertMillisToDate

@Composable
fun BudgetViewScreen(
    navController: NavController,
    customer: Customer?,
    services: List<Pair<String, String>>
) {
    // Recuperar o contexto para usar o DataStore
    val context = LocalContext.current

    // Observar os dados do dono do DataStore
    val ownerFlow = OwnerDataStore.getOwnerData(context)
    val owner by ownerFlow.collectAsState(initial = null)

    val date = convertMillisToDate(System.currentTimeMillis()) // Data atual
    val clientName = customer?.name ?: "Cliente não definido"
    val clientCpfCnpj = customer?.cpfOrCnpj ?: "CPF/CNPJ não definido"
    val phone = customer?.phone ?: "phone não definido"
    val email = customer?.email ?: "email não definido"
    val clientAddress = customer?.address?.getFormattedAddress() ?: "Endereço não definido"

    // Transformar lista de serviços para o formato usado na tabela
    val formattedServices = services.map { (name, value) ->
        mapOf(
            "description" to name,
            "value" to value,
            "quantity" to "1", // Por enquanto, estático
            "total" to value
        )
    }

    // Calcular o total dos serviços
    val totalValue = services.sumOf {
        it.second.replace("R$ ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Cabeçalho com botão de voltar e título
        Header(
            title = stringResource(R.string.view_budget),
            onBackClick = { navController.navigateUp() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Corpo principal com informações do orçamento
        BudgetDetails(
            owner = owner, // Passar os dados do dono (carregados do DataStore)
            date = date,
            clientName = clientName,
            clientCpfCnpj = clientCpfCnpj,
            clientAddress = clientAddress,
            services = formattedServices,
            totalValue = totalValue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botões de ação
        ActionButtons()
    }
}

@Composable
fun Header(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun BudgetDetails(
    owner: Owner?, // Adicionado o dono como parâmetro
    date: String,
    clientName: String,
    clientCpfCnpj: String,
    clientAddress: String,
    services: List<Map<String, String>>,
    totalValue: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Dados do dono do app
        if (owner != null) {
            Text(text = owner.name, fontWeight = FontWeight.Bold)
            Text(text = owner.cpfOrCnpj)
            Text(
                text = owner.address?.let {
                    "${it.street}, ${it.number} - ${it.neighborhood}, ${it.city} - ${it.state}"
                } ?: "Endereço não definido"
            )
        } else {
            Text(
                text = stringResource(R.string.budget),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth() // Faz o texto ocupar toda a largura
                    .align(Alignment.CenterHorizontally) // Centraliza no eixo horizontal
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Adicionando o divisor
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dados do cliente
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${stringResource(R.string.date)}: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = date, fontWeight = FontWeight.Normal)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${stringResource(R.string.client)}: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = clientName, fontWeight = FontWeight.Normal)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "CPF/CNPJ: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = clientCpfCnpj, fontWeight = FontWeight.Normal)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${stringResource(R.string.address)} ",
                fontWeight = FontWeight.Bold
            )
            Text(text = clientAddress, fontWeight = FontWeight.Normal)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.order_details),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tabela de serviços
        Column(modifier = Modifier.fillMaxWidth()) {
            TableHeader()
            LazyColumn {
                items(services.size) { index ->
                    TableRow(service = services[index])
                }
                // Adicionar a linha do total
                item {
                    TotalRow(totalValue = totalValue)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Adicionando a forma de pagamento
        Text(
            text = stringResource(R.string.payment_method),
            fontSize = 14.sp,
        )
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black) // Borda ao redor do cabeçalho
    ) {
        Text(
            text = stringResource(R.string.description),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.value),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.quantity),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .border(0.5.dp, Color.Black),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.total).uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1.5f)
                .border(0.5.dp, Color.Black),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TableRow(service: Map<String, String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
    ) {
        Text(
            text = service["description"] ?: "",
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        Text(
            text = service["value"] ?: "",
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        Text(
            text = service["quantity"] ?: "",
            modifier = Modifier
                .weight(1f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        Text(
            text = service["total"] ?: "",
            modifier = Modifier
                .weight(1.5f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TotalRow(totalValue: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
    ) {
        Text(
            text = "Total:",
            modifier = Modifier
                .weight(5f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )
        Text(
            text = "R$ %.2f".format(totalValue),
            modifier = Modifier
                .weight(1.5f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ActionButtons() {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Lógica para enviar PDF */ }
    ) {
        Text(text = "ENVIAR PDF", fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Lógica para enviar imagem */ }
    ) {
        Text(text = "Enviar Imagem")
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetViewScreenPreview() {
    val customer = Customer(
        name = "Thyago Lobato",
        cpfOrCnpj = "12611121724",
        phone = "15981229370",
        email = "thyagollobato@gmail.com",
        address = CustomerAddress(
            postalCode = "12345-678",
            street = "Rua das Flores",
            number = "100",
            complement = "Casa",
            neighborhood = "Centro",
            city = "São Paulo",
            state = "SP"
        )
    )
    val services = listOf(
        "Marcenaria" to "R$ 100,00",
        "Pintura" to "R$ 200,00"
    )
    BudgetViewScreen(
        navController = rememberNavController(),
        customer = customer,
        services = services
    )
}