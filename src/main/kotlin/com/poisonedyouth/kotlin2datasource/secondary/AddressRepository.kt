package com.poisonedyouth.kotlin2datasource.secondary

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.AUTO
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : CrudRepository<Address, Long>

@Entity
@Table(name = "address")
data class Address(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
    @Column(name = "string")
    val street: String,
    @Column(name = "city")
    val city: String,
    @Column(name = "zip_code")
    val zipCode: Int
)