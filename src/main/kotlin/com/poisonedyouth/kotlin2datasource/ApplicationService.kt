package com.poisonedyouth.kotlin2datasource

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApplicationService(
    private val customerRepository: CustomerRepository,
    private val addressRepository: AddressRepository,
) {

    @Transactional(transactionManager = "chainedTransactionManager", rollbackFor = [IllegalStateException::class])
    fun saveCustomerAndAddress(customer: Customer, address: Address) {
        addressRepository.saveAddress(address)
        customerRepository.saveCustomerWithException(customer)
    }
}