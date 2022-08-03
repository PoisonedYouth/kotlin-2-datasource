package com.poisonedyouth.kotlin2datasource

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(propagation = Propagation.REQUIRED)
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

    fun saveCustomerWithException(customer: Customer): Customer {
        val id = CustomerTable.insertAndGetId {
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[age] = customer.age
        }.value
        throw IllegalStateException("Failed to persist customer with id '$id'.")
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

