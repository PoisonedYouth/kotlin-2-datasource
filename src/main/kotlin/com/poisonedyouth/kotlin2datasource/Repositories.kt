package com.poisonedyouth.kotlin2datasource

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerRepository {

    fun saveCustomer(customer: Customer): Customer {
        val id = CustomerTable.insertAndGetId {
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[age] = customer.age
        }.value
        return customer.copy(
            id = id
        )
    }

    fun findCustomerById(id: Long): Customer? {
        return CustomerTable.select { CustomerTable.id eq id }.singleOrNull()?.let {
            Customer(
                id = it[CustomerTable.id].value,
                firstName = it[CustomerTable.firstName],
                lastName = it[CustomerTable.lastName],
                age = it[CustomerTable.age]
            )
        }
    }
}

data class Customer(
    val id: Long? = null,
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
@Transactional(transactionManager = "CustomDB")
class AddressRepository {

    fun saveAddress(address: Address): Address {
        val id = AddressTable.insertAndGetId {
            it[street] = address.street
            it[city] = address.city
            it[zipCode] = address.zipCode
        }.value
        return address.copy(
            id = id
        )
    }

    fun findAddressById(id: Long): Address? {
        return AddressTable.select { AddressTable.id eq id }.singleOrNull()?.let {
            Address(
                id = it[AddressTable.id].value,
                street = it[AddressTable.street],
                city = it[AddressTable.city],
                zipCode = it[AddressTable.zipCode]
            )
        }
    }
}

object AddressTable : LongIdTable(name = "address", columnName = "id") {
    val street = varchar("street", 255)
    val city = varchar("city", 255)
    val zipCode = integer("zip_code")
}

data class Address(
    val id: Long? = null,
    val street: String,
    val city: String,
    val zipCode: Int
)

