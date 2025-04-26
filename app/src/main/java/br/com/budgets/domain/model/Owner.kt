package br.com.budgets.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val name: String,
    val cpfOrCnpj: String,
    val phone: String?,
    val email: String?,
    val address: OwnerAddress? = null
)
