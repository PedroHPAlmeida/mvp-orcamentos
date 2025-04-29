package br.com.budgets.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers_addresses")
data class CustomerAddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var customerId: Long = 0,
    val postalCode: String,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String
)