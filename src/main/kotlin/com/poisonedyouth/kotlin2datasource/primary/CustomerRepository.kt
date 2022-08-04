package com.poisonedyouth.kotlin2datasource.primary

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Repository
@Transactional(propagation = Propagation.REQUIRED)
interface CustomerRepository : CrudRepository<Customer, Long>


@Repository
@Transactional(propagation = Propagation.REQUIRED)
class CustomerRepositoryCustom(
    private val customerRepository: CustomerRepository
) {

    fun saveWithException(customer: Customer) {
        customerRepository.save(customer)
        throw IllegalStateException("Failed!")
    }
}

@Entity
@Table(name = "customer")
data class Customer(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "last_name")
    val lastName: String,
    @Column(name = "age")
    val age: Int
)

