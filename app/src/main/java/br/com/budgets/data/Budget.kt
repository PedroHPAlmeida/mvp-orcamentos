package br.com.budgets.data

data class Budget(
    val id: Long,
    val customer: String,
    val productsAndServices: Int,
    val value: Double,
)
