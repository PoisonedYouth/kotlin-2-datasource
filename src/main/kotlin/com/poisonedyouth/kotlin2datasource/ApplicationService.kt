package com.poisonedyouth.kotlin2datasource

import com.poisonedyouth.kotlin2datasource.primary.Customer
import com.poisonedyouth.kotlin2datasource.primary.CustomerRepository
import com.poisonedyouth.kotlin2datasource.primary.CustomerRepositoryCustom
import com.poisonedyouth.kotlin2datasource.secondary.Address
import com.poisonedyouth.kotlin2datasource.secondary.AddressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApplicationService(
    private val customerRepository: CustomerRepositoryCustom,
    private val addressRepository: AddressRepository,
) {

    @Transactional(transactionManager = "jtaTransactionManager", rollbackFor = [IllegalStateException::class])
    fun saveCustomerAndAddress(customer: Customer, address: Address) {
        addressRepository.save(address)
        customerRepository.saveWithException(customer)
    }
}