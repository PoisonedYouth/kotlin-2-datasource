package com.poisonedyouth.kotlin2datasource

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI


@RestController
class Restcontroller(
    private val customerRepository: CustomerRepository,
    private val addressRepository: AddressRepository,
    private val applicationService: ApplicationService
) {

    @PostMapping(
        "/api/v1/customer",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addCustomer(@RequestBody customer: Customer): ResponseEntity<Any> {
        val customerNew = customerRepository.saveCustomer(customer)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(customerNew.id).toUri()

        return ResponseEntity.created(location).body(customerNew)
    }

    @GetMapping(
        "/api/v1/customer/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCustomerById(@RequestParam id: Long): ResponseEntity<Customer> {
        return ResponseEntity.ok(customerRepository.findCustomerById(id))
    }

    @PostMapping(
        "/api/v1/address",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addAddress(@RequestBody address: Address): ResponseEntity<Any> {
        val addressNew = addressRepository.saveAddress(address)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(addressNew.id).toUri()

        return ResponseEntity.created(location).body(addressNew)
    }

    @GetMapping(
        "/api/v1/address/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAddressById(@PathVariable id: Long): ResponseEntity<Address> {
        return ResponseEntity.ok(addressRepository.findAddressById(id))
    }

    @GetMapping(
        "/api/v1/roleback",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun rollback() : ResponseEntity<Any>{
        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            age = 5
        )

        val address = Address(
            street = "Main Street",
            city = "Los Angeles",
            zipCode = 90001
        )

        return try {
            applicationService.saveCustomerAndAddress(customer, address)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}