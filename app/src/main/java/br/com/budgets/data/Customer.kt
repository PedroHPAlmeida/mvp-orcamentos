package br.com.budgets.data

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val name: String,
    val cpfOrCnpj: String,
    val phone: String,
    val email: String,
    val address: CustomerAddress? = null
)
