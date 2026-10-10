package com.example.taskmanager.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    val tasks: Flow<List<Task>> = dao.observeAll()

    fun getTask(id: Int): Flow<Task?> = dao.observeById(id)

    suspend fun save(task: Task) {
        if (task.id == 0) dao.insert(task) else dao.update(task)
    }

    suspend fun delete(id: Int) = dao.deleteById(id)

    suspend fun updateStatus(id: Int, status: TaskStatus) = dao.updateStatus(id, status)
}