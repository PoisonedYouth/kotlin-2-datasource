package com.poisonedyouth.kotlin2datasource

import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent

@SpringBootApplication
class Kotlin2DatasourceApplication

fun main(args: Array<String>) {
    runApplication<Kotlin2DatasourceApplication>(*args)
}
