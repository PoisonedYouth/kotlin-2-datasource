package com.poisonedyouth.kotlin2datasource

import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component

@Component
class StartupComponent: ApplicationListener<ApplicationStartedEvent> {

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        val datasource1: DataSource = event.applicationContext.getBean("primaryDataSource") as DataSource
        val database1 = Database.connect(datasource1)
        transaction(database1) {
            SchemaUtils.createMissingTablesAndColumns(CustomerTable)
        }

        val datasource2: DataSource = event.applicationContext.getBean("secondaryDataSource") as DataSource
        val database2 = Database.connect(datasource2)
        transaction(database2) {
            SchemaUtils.createMissingTablesAndColumns(AddressTable)
        }

    }
}