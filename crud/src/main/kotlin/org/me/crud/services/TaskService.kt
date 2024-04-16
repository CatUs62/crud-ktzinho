package org.me.crud.services

import org.me.crud.domain.TaskEntity
import org.me.crud.payloads.request.TaskDTORequest
import org.me.crud.payloads.response.TaskDTOResponse
import org.me.crud.repositories.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class TaskService(var repository: TaskRepository) {
    fun findAll(): List<TaskDTOResponse> {
        val tasks = repository.findAll()
        return tasks.map {
            TaskDTOResponse(id = it.id!!, name = it.name, description = it.description, done = it.done)
        }
    }

    fun findById(id: Long): TaskDTOResponse? {
        return repository.findById(id).map {
            TaskDTOResponse(id = it.id!!, name = it.name, description = it.description, done = it.done)
        }.getOrNull()
    }

    @Transactional
    fun create(task: TaskDTORequest): TaskDTOResponse {
        val save = repository.save(
            TaskEntity(id = null, name = task.name, description = task.description, done = task.done)
        )
        return TaskDTOResponse(id = save.id!!, name = save.name, description = save.description, done = save.done)
    }

    @Transactional
    fun update(id: Long, task: TaskDTORequest): TaskDTOResponse? {
        return repository.findById(id).map { existingTask ->
            existingTask.name = task.name
            existingTask.description = task.description
            existingTask.done = task.done
            val updatedTask = repository.save(existingTask)
            TaskDTOResponse(id = updatedTask.id!!, name = updatedTask.name, description = updatedTask.description, done = updatedTask.done)
        }.orElse(null)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }
}