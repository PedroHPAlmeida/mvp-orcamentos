package br.com.budgets.data.repository

import br.com.budgets.data.local.room.dao.CustomerDao
import br.com.budgets.data.local.room.entity.CustomerAddressEntity
import br.com.budgets.data.local.room.entity.CustomerEntity
import br.com.budgets.domain.model.Customer
import br.com.budgets.domain.model.CustomerAddress

class CustomerRepository(private val dao: CustomerDao) {
    val customersFlow = dao.getAllCustomersFlow()
    val customersAndAddressFlow = dao.getAllCustomerAndAddressFlow()

    suspend fun addCustomerAndAddress(
        customer: Customer, customerAddress: CustomerAddress?
    ) {
        val customerId = dao.insertCustomer(CustomerEntity.fromDomain(customer))

        if (customerAddress != null) {
            val customerAddressEntity =
                CustomerAddressEntity.fromDomain(customerAddress, customerId)
            val addressId = dao.insertCustomerAddress(customerAddressEntity)
            customerAddress.copy(id = addressId)
        }

        customer.copy(id = customerId)
    }
}