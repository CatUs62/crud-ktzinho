package org.me.crud.repositories

import org.me.crud.domain.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<TaskEntity, Long> {}