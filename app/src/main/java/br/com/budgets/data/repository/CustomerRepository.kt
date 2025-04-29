package br.com.budgets.data.repository

import br.com.budgets.data.local.room.dao.CustomerDao
import br.com.budgets.data.local.room.entity.CustomerAddressEntity
import br.com.budgets.data.local.room.entity.CustomerEntity

class CustomerRepository(private val dao: CustomerDao) {
    val customersFlow = dao.getAllCustomersFlow()
    val customersAndAddressFlow = dao.getAllCustomerAndAddressFlow()

    suspend fun addCustomerAndAddress(
        customer: CustomerEntity, customerAddress: CustomerAddressEntity?
    ): CustomerEntity {
        val customerId = dao.insertCustomer(customer)

        if (customerAddress != null) {
            customerAddress.customerId = customerId
            dao.insertCustomerAddress(customerAddress)
        }

        customer.copy(id = customerId)
        return customer
    }
}