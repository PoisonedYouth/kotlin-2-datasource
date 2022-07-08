package com.poisonedyouth.kotlin2datasource

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class Restcontroller(
    private val customerRepository: CustomerRepository,
    private val addressRepository: AddressRepository
) {

    @PostMapping(
        "/api/v1/customer",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addCustomer(@RequestBody customer: Customer): ResponseEntity<Long> {
        return ResponseEntity.ok(customerRepository.saveCustomer(customer))
    }

    @PostMapping(
        "/api/v1/address",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addAddress(@RequestBody address: Address): ResponseEntity<Long> {
        return ResponseEntity.ok(addressRepository.saveAddress(address))
    }
}