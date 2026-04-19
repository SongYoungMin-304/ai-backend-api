package com.project.backendapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApiApplication

fun main(args: Array<String>) {
    runApplication<BackendApiApplication>(*args)
}
