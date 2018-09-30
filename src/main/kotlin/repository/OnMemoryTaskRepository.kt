package com.example.repository

import com.example.Task
import com.example.TaskRepository

class OnMemoryTaskRepository: TaskRepository {
    private val tasks: MutableList<Task> = mutableListOf(
            Task(1, "タスク1", false),
            Task(2, "タスク2", false),
            Task(3, "タスク2", true)
    )

    override fun all(done: Boolean?): List<Task> {
        return if (done != null) {
            tasks.filter { it.done == done }
        } else {
            tasks
        }
    }

    override fun save(task: Task): Long {
        val id = task.id ?: nextId()
        val newTask = task.copy(id = id)

        val index: Int = tasks.indexOfFirst { it.id == newTask.id }

        if (index != -1) {
            tasks.removeAt(index)

        }

        tasks.add(newTask)

        return id
    }

    override fun findById(id: Long?): Task? {
        return tasks.find { it.id == id }
    }

    fun nextId(): Long {
        return tasks.map { it.id }.filterNotNull().max()?.plus(1) ?: 0
    }
}