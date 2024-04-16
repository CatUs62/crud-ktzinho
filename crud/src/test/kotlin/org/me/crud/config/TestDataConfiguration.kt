package org.me.crud.config

import org.me.crud.domain.TaskEntity
import org.me.crud.repositories.TaskRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestDataConfiguration {
    @Bean
    fun preLoadTestData(taskRepository: TaskRepository): CommandLineRunner {
        return CommandLineRunner {
            val taskOne = TaskEntity(id = 1L, name = "Alyasaf", description = "Descrição", done = false)
            taskRepository.save(taskOne)
        }
    }
}