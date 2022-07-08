package com.poisonedyouth.kotlin2datasource

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerRepository {

    fun saveCustomer(customer: Customer): Long {
        SchemaUtils.createMissingTablesAndColumns(CustomerTable)
        return CustomerTable.insertAndGetId {
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[age] = customer.age
        }.value
    }
}

data class Customer(
    val firstName: String,
    val lastName: String,
    val age: Int
)

object CustomerTable : LongIdTable(name = "customer", columnName = "id") {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val age = integer("age")
}

@Repository
@Transactional(transactionManager = "AddressDB")
class AddressRepository {
    fun saveAddress(address: Address): Long {
        SchemaUtils.createMissingTablesAndColumns(AddressTable)
        return AddressTable.insertAndGetId {
            it[street] = address.street
            it[city] = address.city
            it[zipCode] = address.zipCode
        }.value
    }
}

object AddressTable : LongIdTable(name = "address", columnName = "id") {
    val street = varchar("street", 255)
    val city = varchar("city", 255)
    val zipCode = integer("zip_code")
}

data class Address(
    val street: String,
    val city: String,
    val zipCode: Int
)

