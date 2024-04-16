package org.me.crud.resources

import org.me.crud.payloads.request.TaskDTORequest
import org.me.crud.payloads.response.TaskDTOResponse
import org.me.crud.services.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/tasks")
class TaskResource (val taskService: TaskService){
    @GetMapping
    fun getAllTasks(): ResponseEntity<List<TaskDTOResponse>> {
        val listTaks = taskService.findAll()
        return ResponseEntity.ok().body(listTaks)
    }

    @GetMapping("/{id}")
    fun getTaskId(@PathVariable id: Long): ResponseEntity<TaskDTOResponse> {
        val task = taskService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found: $id")
        return ResponseEntity.ok().body(task)
    }

    @PostMapping("/create")
    fun createTask(@RequestBody task: TaskDTORequest): ResponseEntity<TaskDTOResponse> {
        val taskCreated = taskService.create(task)
        val uri: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("{name}")
            .buildAndExpand(task.name)
            .toUri()

        return ResponseEntity.created(uri).body(taskCreated)
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody task: TaskDTORequest): ResponseEntity<TaskDTOResponse> {
        val taskUpdate = taskService.update(id, task) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found: $id")
        return ResponseEntity.ok().body(taskUpdate)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long) {
        taskService.delete(id)
    }
}