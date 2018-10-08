package com.example

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*

data class NewTaskParam(val name: String)

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
                "false" -> taskList.filterNot { it.done }
                else -> taskList
            }

            call.respond(response)
        }

        post("/tasks") {
            val taskParam: NewTaskParam = call.receive<NewTaskParam>()
            val newId: Long = taskList.mapNotNull { task -> task.id }.max()?.plus(1) ?: 0
            val task: Task = Task(newId, taskParam.name, false)

            taskList.add(task)

            call.respond(HttpStatusCode.OK, newId)
        }
    }
}