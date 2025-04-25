package br.com.budgets.data

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val name: String,
    val cpfOrCnpj: String,
    val address: OwnerAddress? = null
)
