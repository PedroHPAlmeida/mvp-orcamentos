package br.com.budgets.data

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val name: String,
    val cpfOrCnpj: String,
)
