package com.poisonedyouth.kotlin2datasource

import javax.sql.DataSource
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Database.Companion
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.REQUIRED
import org.springframework.transaction.annotation.Transactional

@Repository
class AddressRepository(@SecondaryDataSource private val dataSource: DataSource) {

    fun saveAddress(address: Address): Address =
        transaction(Database.connect(dataSource)) {
            val id = AddressTable.insertAndGetId {
                it[street] = address.street
                it[city] = address.city
                it[zipCode] = address.zipCode
            }.value
            return@transaction address.copy(
                id = id
            )
        }

    fun findAddressById(id: Long): Address? = transaction(Companion.connect(dataSource)) {
        AddressTable.select { AddressTable.id eq id }.singleOrNull()?.let {
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