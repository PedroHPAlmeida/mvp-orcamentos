package br.com.budgets.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: Long = 0,
    val name: String,
    val cpfOrCnpj: String,
    val phone: String,
    val email: String,
    val address: CustomerAddress? = null
)
