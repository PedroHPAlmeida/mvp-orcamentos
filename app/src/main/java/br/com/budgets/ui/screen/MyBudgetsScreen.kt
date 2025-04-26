package br.com.budgets.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.budgets.R
import br.com.budgets.domain.model.Budget

val budgets = listOf(
    Budget(1, "João", 2, 100.0),
    Budget(2, "Maria", 1, 50.0),
    Budget(3, "Pedro", 3, 150.0),
    Budget(4, "João", 2, 100.0),
    Budget(5, "Maria", 1, 50.0),
    Budget(6, "Pedro", 3, 150.0),
    Budget(7, "João", 2, 100.0),
    Budget(8, "Maria", 1, 50.0),
    Budget(9, "Pedro", 3, 150.0),
    Budget(10, "João", 2, 100.0),
    Budget(11, "Maria", 1, 50.0),
    Budget(12, "Pedro", 3, 150.0),
    Budget(13, "João", 2, 100.0),
    Budget(14, "Maria", 1, 50.0),
    Budget(15, "Pedro", 3, 150.0),
    Budget(16, "João", 2, 100.0),
    Budget(17, "Maria", 1, 50.0),
    Budget(18, "Pedro", 3, 150.0),
    Budget(19, "João", 2, 100.0),
    Budget(20, "Maria", 1, 50.0),
)

@Composable
fun MyBudgetsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(budgets.size) { index ->
                BudgetItem(budgets[index])
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Navega para a tela de novo orçamento
                navController.navigate("new_budget")
            }
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.new_budget_button))
        }
    }
}

@Composable
fun BudgetItem(budget: Budget) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {}
                .padding(8.dp)) {
            Text(
                text = stringResource(R.string.budget_and_id, budget.id),
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.customer), fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = budget.customer)
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.quantity_prod_and_serv),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = budget.productsAndServices.toString())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.budget_value), fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "R$ ${budget.value}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyBudgetsScreenPreview() {
    MyBudgetsScreen(navController = rememberNavController())
}