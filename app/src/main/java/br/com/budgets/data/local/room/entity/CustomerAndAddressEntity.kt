package br.com.budgets.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Relation


data class CustomerAndAddressEntity(
    @Embedded val customer: CustomerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "customerId"
    )
    val address: CustomerAddressEntity
) {}