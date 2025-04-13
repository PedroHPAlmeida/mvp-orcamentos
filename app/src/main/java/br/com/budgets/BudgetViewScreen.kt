package br.com.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun BudgetViewScreen(navController: NavController) {
    // Mock de dados dinâmicos
    val companyName = "Serviços LTDA"
    val companyContact = "15981229330 - servicos@email.com"
    val companyAddress = "Rua Agora Vai, 1000"
    val date = "27 de março de 2025"
    val clientName = "Thyago Lobato"
    val clientCpfCnpj = "12611121724"
    val clientAddress = "Rua Agora Foi, 10"

    // Mock dos serviços como uma lista de mapas
    val services = listOf(
        mapOf(
            "description" to "Marcenaria",
            "value" to "R$ 100,00",
            "quantity" to "1",
            "total" to "R$ 100,00"
        ),
        mapOf(
            "description" to "Pintura",
            "value" to "R$ 200,00",
            "quantity" to "2",
            "total" to "R$ 400,00"
        )
    )

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
            companyName = companyName,
            companyContact = companyContact,
            companyAddress = companyAddress,
            date = date,
            clientName = clientName,
            clientCpfCnpj = clientCpfCnpj,
            clientAddress = clientAddress,
            services = services
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
    companyName: String,
    companyContact: String,
    companyAddress: String,
    date: String,
    clientName: String,
    clientCpfCnpj: String,
    clientAddress: String,
    services: List<Map<String, String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Dados do dono do app
        Text(text = companyName, fontWeight = FontWeight.Bold)
        Text(text = companyContact)
        Text(text = companyAddress)

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
                text = "${stringResource(R.string.address)}: ",
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
                .border(0.5.dp, Color.Black), // Borda entre colunas
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.value),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black), // Borda entre colunas
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.quantity),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .border(0.5.dp, Color.Black), // Borda entre colunas
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.total).uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1.5f)
                .border(0.5.dp, Color.Black), // Borda entre colunas
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
        // Coluna "Descrição"
        Column(
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp)
        ) {
            Text(
                text = service["description"] ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Coluna "Valor"
        Column(
            modifier = Modifier
                .weight(2f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp)
        ) {
            Text(
                text = service["value"] ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Coluna "QTD"
        Column(
            modifier = Modifier
                .weight(1f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp)
        ) {
            Text(
                text = service["quantity"] ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Coluna "Total"
        Column(
            modifier = Modifier
                .weight(1.5f)
                .border(0.5.dp, Color.Black)
                .padding(4.dp)
        ) {
            Text(
                text = service["total"] ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }
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
    BudgetViewScreen(navController = rememberNavController())
}