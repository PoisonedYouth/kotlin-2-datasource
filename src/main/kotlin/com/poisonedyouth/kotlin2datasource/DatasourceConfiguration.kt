package com.poisonedyouth.kotlin2datasource

import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import com.atomikos.jdbc.AtomikosDataSourceBean
import javax.sql.DataSource
import javax.transaction.UserTransaction
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager


@Configuration
@EnableJpaRepositories(
    basePackages = ["com.poisonedyouth.kotlin2datasource.primary"],
    entityManagerFactoryRef = "primaryEntityManager",
    transactionManagerRef = "jtaTransactionManager"
)
class DatasourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    fun primaryDataSource(): DataSource {
        return AtomikosDataSourceBean()
    }

    @Bean("primaryEntityManager")
    @Primary
    fun primaryEntityManager(jpaVendorAdapter: JpaVendorAdapter): LocalContainerEntityManagerFactoryBean? {
        val properties: HashMap<String, Any?> = HashMap()
        properties["hibernate.dialect"] = "org.hibernate.dialect.MySQL57Dialect"
        properties["hibernate.default_schema"] = "public"
        properties["hibernate.hbm2ddl.auto"] = "create"
        properties["hibernate.show_sql"] = true
        properties["hibernate.format_sql"] = true
        val entityManager = LocalContainerEntityManagerFactoryBean()
        entityManager.setJtaDataSource(primaryDataSource())
        entityManager.jpaVendorAdapter = jpaVendorAdapter
        entityManager.setPackagesToScan("com.poisonedyouth.kotlin2datasource.primary")
        entityManager.persistenceUnitName = "primaryEntityManager"
        entityManager.setJpaPropertyMap(properties)
        return entityManager
    }
}


@Configuration
@EnableJpaRepositories(
    basePackages = ["com.poisonedyouth.kotlin2datasource.secondary"],
    entityManagerFactoryRef = "secondaryEntityManager", transactionManagerRef = "jtaTransactionManager"
)
class DatasourceConfigurationSecondary {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    fun secondaryDataSource(): DataSource {
        return AtomikosDataSourceBean()
    }

    @Bean("secondaryEntityManager")
    fun secondaryEntityManager(jpaVendorAdapter: JpaVendorAdapter): LocalContainerEntityManagerFactoryBean? {
        val properties: HashMap<String, Any?> = HashMap()
        properties["hibernate.dialect"] = "org.hibernate.dialect.MySQL57Dialect"
        properties["hibernate.default_schema"] = "public"
        properties["hibernate.hbm2ddl.auto"] = "create"
        properties["hibernate.show_sql"] = true
        properties["hibernate.format_sql"] = true
        val entityManager = LocalContainerEntityManagerFactoryBean()
        entityManager.setJtaDataSource(secondaryDataSource())
        entityManager.jpaVendorAdapter = jpaVendorAdapter
        entityManager.setPackagesToScan("com.poisonedyouth.kotlin2datasource.secondary")
        entityManager.persistenceUnitName = "secondaryEntityManager"
        entityManager.setJpaPropertyMap(properties)
        return entityManager
    }
}

@Configuration
@EnableTransactionManagement
class JtaConfiguration {
    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter? {
        val hibernateJpaVendorAdapter = HibernateJpaVendorAdapter()
        hibernateJpaVendorAdapter.setShowSql(false)
        return hibernateJpaVendorAdapter
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    fun userTransactionManager(): UserTransactionManager {
        val userTransactionManager = UserTransactionManager()
        userTransactionManager.setTransactionTimeout(300)
        userTransactionManager.forceShutdown = true
        return userTransactionManager
    }

    @Bean("jtaTransactionManager")
    fun transactionManager(): JtaTransactionManager {
        val jtaTransactionManager = JtaTransactionManager()
        jtaTransactionManager.transactionManager = userTransactionManager()
        jtaTransactionManager.userTransaction = userTransactionManager()
        return jtaTransactionManager
    }

    @Bean
    fun userTransaction(): UserTransaction {
        val userTransaction = UserTransactionImp()
        userTransaction.setTransactionTimeout(60000)
        return userTransaction
    }
}

