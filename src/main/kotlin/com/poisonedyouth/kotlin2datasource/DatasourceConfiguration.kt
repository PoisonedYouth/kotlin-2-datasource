package com.poisonedyouth.kotlin2datasource

import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import com.atomikos.jdbc.AtomikosDataSourceBean
import javax.sql.DataSource
import javax.transaction.UserTransaction
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager


@Qualifier("primary")
annotation class PrimaryDataSource

@Qualifier("secondary")
annotation class SecondaryDataSource


@Configuration
@EnableTransactionManagement
class DatasourceConfiguration {

    @Bean
    @PrimaryDataSource
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    fun primaryDataSource(): DataSource {
        return AtomikosDataSourceBean()
    }

    @Bean
    @SecondaryDataSource
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    fun secondaryDataSource(): DataSource {
        return AtomikosDataSourceBean()
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    fun userTransactionManager(): UserTransactionManager {
        val userTransactionManager = UserTransactionManager()
        userTransactionManager.setTransactionTimeout(300)
        userTransactionManager.forceShutdown = true
        return userTransactionManager
    }

    @Bean
    @Primary
    fun jtaTransactionManager(): JtaTransactionManager {
        val jtaTransactionManager = JtaTransactionManager()
        jtaTransactionManager.transactionManager = userTransactionManager()
        jtaTransactionManager.userTransaction = userTransactionManager()
        return jtaTransactionManager
    }

    @Bean
    fun userTransaction(): UserTransaction? {
        val userTransaction = UserTransactionImp()
        userTransaction.setTransactionTimeout(60000)
        return userTransaction
    }
}

