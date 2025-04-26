package br.com.budgets.domain.model

data class Budget(
    val id: Long,
    val customer: String,
    val productsAndServices: Int,
    val value: Double,
)
