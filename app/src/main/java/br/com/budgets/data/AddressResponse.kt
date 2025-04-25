package br.com.budgets.data

import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?
)