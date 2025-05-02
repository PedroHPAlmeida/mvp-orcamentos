package br.com.budgets.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.budgets.domain.model.CustomerAddress

@Entity(tableName = "customers_addresses")
data class CustomerAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var customerId: Long = 0,
    val postalCode: String,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String
) {
    fun toDomain(): CustomerAddress {
        return CustomerAddress(
            postalCode = postalCode,
            street = street,
            number = number,
            complement = complement,
            neighborhood = neighborhood,
            city = city,
            state = state
        )
    }

    companion object {
        fun fromDomain(customerAddress: CustomerAddress, customerId: Long): CustomerAddressEntity {
            return CustomerAddressEntity(
                id = customerAddress.id,
                customerId = customerId,
                postalCode = customerAddress.postalCode,
                street = customerAddress.street,
                number = customerAddress.number,
                complement = customerAddress.complement,
                neighborhood = customerAddress.neighborhood,
                city = customerAddress.city,
                state = customerAddress.state
            )
        }
    }
}