package br.com.budgets.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProductServiceItem(
    name: String,
    value: String,
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onDelete: () -> Unit
) {
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
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nome e valor do produto/serviço
            Column(
                modifier = Modifier.weight(3f) // Toma mais espaço no card
            ) {
                Text(text = name, fontWeight = FontWeight.Bold)
                Text(text = "Valor unitário: $value")
            }

            // Botões para ajustar a quantidade
            Row(
                modifier = Modifier.weight(2f), // Espaço médio no card
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Botão de diminuir quantidade
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(50)) // Botão circular
                        .background(MaterialTheme.colorScheme.primary) // Fundo do botão
                        .clickable { onDecreaseQuantity() },
                    contentAlignment = Alignment.Center // Centralizar o ícone
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove, // Ícone de "-"
                        contentDescription = "Diminuir quantidade",
                        tint = MaterialTheme.colorScheme.onPrimary // Garante visibilidade
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Exibição da quantidade
                Text(
                    text = quantity.toString(),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botão de aumentar quantidade
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(50)) // Botão circular
                        .background(MaterialTheme.colorScheme.primary) // Fundo do botão
                        .clickable { onIncreaseQuantity() },
                    contentAlignment = Alignment.Center // Centralizar o ícone
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, // Ícone de "+"
                        contentDescription = "Aumentar quantidade",
                        tint = MaterialTheme.colorScheme.onPrimary // Garante visibilidade
                    )
                }
            }

            // Ícone de exclusão
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir item",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete() }
            )
        }
    }
}