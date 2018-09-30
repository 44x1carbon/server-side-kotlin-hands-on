package com.example

import com.example.repository.OnMemoryTaskRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
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
        val taskRepository: TaskRepository = OnMemoryTaskRepository()

        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        get("/tasks") {
            val queryParameters = call.request.queryParameters

            val done = when(queryParameters.get("done")) {
                "true" -> true
                "false" -> false
                else -> null
            }

            val response = taskRepository.all(done)

            call.respond(response)
        }

        post("/tasks") {
            val taskParam = call.receive<NewTaskParam>()
            val task = Task(null, taskParam.name, false)

            val newId = taskRepository.save(task)

            call.respond(HttpStatusCode.Created, newId)
        }

        put("/tasks/{id}/done") {
            val id = call.parameters["id"]?.toLong()
            val task = taskRepository.findById(id)

            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }

            val doneTaskParam = call.receive<DoneTaskParam>()
            taskRepository.save(task.copy(done = doneTaskParam.done))
            call.respond(HttpStatusCode.OK, mapOf("id" to task.id.toString()))
        }
    }
}