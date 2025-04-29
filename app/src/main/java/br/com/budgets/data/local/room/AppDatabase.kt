package br.com.budgets.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.budgets.data.local.room.dao.CustomerDao
import br.com.budgets.data.local.room.entity.CustomerAddressEntity
import br.com.budgets.data.local.room.entity.CustomerEntity

@Database(
    entities = [CustomerEntity::class, CustomerAddressEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
}