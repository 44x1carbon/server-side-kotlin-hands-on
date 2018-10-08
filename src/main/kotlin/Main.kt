package com.example

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.response.*
import io.ktor.routing.*

fun Application.main() {
    install(ContentNegotiation) {
        gson {}
    }

    routing {
        val taskList = mutableListOf(
                Task(1, "タスク1", false),
                Task(2, "タスク2", false),
                Task(3, "タスク3", true)
        )

        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }

        get("/tasks") {
            val queryParameters: Parameters = call.request.queryParameters
            val done: String? = queryParameters["done"]

            val response: List<Task> = when(done) {
                "true" -> taskList.filter { it.done }
                else -> taskList
            }

            call.respond(response)
        }
    }
}