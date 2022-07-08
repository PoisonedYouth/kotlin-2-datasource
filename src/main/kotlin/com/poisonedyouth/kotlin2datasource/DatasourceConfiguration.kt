package com.poisonedyouth.kotlin2datasource

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableTransactionManagement
class DatasourceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    fun primaryHikariConfig(): HikariConfig? {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun primaryDataSource(): DataSource {
        return HikariDataSource(primaryHikariConfig())
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    fun secondaryHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun secondaryDataSource(): DataSource {
        return HikariDataSource(secondaryHikariConfig())
    }

    @Bean
    @Qualifier("CustomerDB")
    @Primary
    fun primaryTransactionManager() = SpringTransactionManager(primaryDataSource())

    @Bean
    @Qualifier("AddressDB")
    fun secondaryTransactionManager() = SpringTransactionManager(secondaryDataSource())
}

