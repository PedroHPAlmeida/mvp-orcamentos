package br.com.budgets.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.budgets.data.local.room.entity.CustomerAddressEntity
import br.com.budgets.data.local.room.entity.CustomerAndAddressEntity
import br.com.budgets.data.local.room.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers")
    fun getAllCustomersFlow(): Flow<List<CustomerEntity>>

    @Transaction
    @Query("SELECT * FROM customers")
    fun getAllCustomerAndAddressFlow(): Flow<List<CustomerAndAddressEntity>>

    // TODO: understand the best conflict strategy for our use case
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCustomerAddress(customerAddress: CustomerAddressEntity): Long
}