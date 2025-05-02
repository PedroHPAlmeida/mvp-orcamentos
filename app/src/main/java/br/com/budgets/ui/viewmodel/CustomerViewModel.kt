package br.com.budgets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.budgets.data.local.room.entity.CustomerAndAddressEntity
import br.com.budgets.data.local.room.entity.CustomerEntity
import br.com.budgets.data.repository.CustomerRepository
import br.com.budgets.domain.model.Customer
import br.com.budgets.domain.model.CustomerAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {
    private val _customers = MutableStateFlow<List<CustomerEntity>>(emptyList())
    val customers: StateFlow<List<CustomerEntity>> = _customers.asStateFlow()

    private val _customersAndAddress = MutableStateFlow<List<CustomerAndAddressEntity>>(emptyList())
    val customersAndAddress: StateFlow<List<CustomerAndAddressEntity>> =
        _customersAndAddress.asStateFlow()

    init {
        viewModelScope.launch {
            repository.customersFlow.collect { list ->
                _customers.value = list
            }
            repository.customersAndAddressFlow.collect { list ->
                _customersAndAddress.value = list
            }
        }
    }

    suspend fun addCustomer(
        customer: Customer,
        customerAddress: CustomerAddress? = null
    ) {
        repository.addCustomerAndAddress(customer, customerAddress)
    }
}