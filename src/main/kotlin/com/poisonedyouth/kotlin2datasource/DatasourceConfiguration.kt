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
import org.springframework.data.transaction.ChainedTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Qualifier("primary")
annotation class PrimaryDataSource

@Qualifier("secondary")
annotation class SecondaryDataSource


@Configuration
@EnableTransactionManagement
class DatasourceConfiguration {
    @Bean
    @Primary
    @PrimaryDataSource
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    fun primaryHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @PrimaryDataSource
    @Primary
    fun primaryDataSource(
        @PrimaryDataSource hikariConf: HikariConfig
    ): DataSource {
        return HikariDataSource(hikariConf)
    }

    @Bean("primaryTransactionManager")
    @Primary
    @PrimaryDataSource
    fun primaryTransactionManager(
        @PrimaryDataSource dataSource: DataSource
    ): SpringTransactionManager {
        return SpringTransactionManager(dataSource, showSql = true)
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    fun secondaryHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @SecondaryDataSource
    fun secondaryDataSource(
        hikariConf: HikariConfig
    ): DataSource {
        return HikariDataSource(hikariConf)
    }

    @Bean("secondaryTransactionManager")
    @SecondaryDataSource
    fun secondaryTransactionManager(@SecondaryDataSource dataSource: DataSource): SpringTransactionManager {
        return SpringTransactionManager(
            _dataSource = dataSource, showSql = true
        )
    }

    @Bean("chainedTransactionManager")
    fun transactionManager(
        @SecondaryDataSource ds1: SpringTransactionManager,
        @PrimaryDataSource ds2: SpringTransactionManager
    ): ChainedTransactionManager? {
        return ChainedTransactionManager(ds1, ds2)
    }
}

