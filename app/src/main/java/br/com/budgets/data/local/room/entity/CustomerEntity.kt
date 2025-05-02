package br.com.budgets.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.budgets.domain.model.Customer

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cpfOrCnpj: String,
    val phone: String,
    val email: String,
) {
    fun toDomain(address: CustomerAddressEntity?): Customer {
        if (address != null) {
            return Customer(
                id = id,
                name = name,
                cpfOrCnpj = cpfOrCnpj,
                phone = phone,
                email = email,
                address = address.toDomain()
            )
        }

        return Customer(
            id = id,
            name = name,
            cpfOrCnpj = cpfOrCnpj,
            phone = phone,
            email = email
        )
    }

    companion object {
        fun fromDomain(customer: Customer): CustomerEntity {
            return CustomerEntity(
                id = customer.id,
                name = customer.name,
                cpfOrCnpj = customer.cpfOrCnpj,
                phone = customer.phone,
                email = customer.email
            )
        }
    }
}
