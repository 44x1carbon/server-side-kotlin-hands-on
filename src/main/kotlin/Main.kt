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
data class DoneTaskParam(val done: Boolean)

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

        patch("/tasks/{id}/done") {
            val doneTaskParam: DoneTaskParam = call.receive<DoneTaskParam>()
            val id: Long? = call.parameters["id"]?.toLong()

            val index: Int = taskList.indexOfFirst { it.id == id }

            if (index == -1) {
                call.respond(HttpStatusCode.NotFound)
                return@patch
            }

            taskList[index] = taskList[index].copy(done = doneTaskParam.done)

            call.respond(HttpStatusCode.OK)
        }

    }
}