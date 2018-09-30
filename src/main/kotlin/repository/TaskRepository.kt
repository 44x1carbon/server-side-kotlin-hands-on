package com.example

interface TaskRepository {
    fun all(done: Boolean? = null): List<Task>
    fun save(task: Task): Long
    fun findById(id: Long?): Task?
}