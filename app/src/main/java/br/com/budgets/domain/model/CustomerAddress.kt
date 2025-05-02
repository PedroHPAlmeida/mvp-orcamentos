package br.com.budgets.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomerAddress(
    val id: Long = 0,
    val postalCode: String,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String
) {

    fun getFormattedAddress(): String {
        val baseAddress = "$street, $number - $city/$state"
        return if (!complement.isNullOrBlank()) {
            "$baseAddress ($complement)"
        } else {
            baseAddress
        }
    }
}