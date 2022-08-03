package com.poisonedyouth.kotlin2datasource

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.REQUIRED
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "secondaryTransactionManager", propagation = REQUIRED)
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