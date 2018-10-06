package com.example

import io.ktor.application.*
import io.ktor.http.ContentType
import io.ktor.response.*
import io.ktor.routing.*

fun Application.main() {
    routing {
        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
    }
}