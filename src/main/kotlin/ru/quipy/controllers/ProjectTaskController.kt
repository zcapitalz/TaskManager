package ru.quipy.controllers

import org.springframework.web.bind.annotation.*
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.events.project.TaskAssignedEvent
import ru.quipy.api.events.project.TaskCreatedEvent
import ru.quipy.api.events.project.TaskStatusChangedEvent
import ru.quipy.api.events.project.TaskUnassignedEvent
import ru.quipy.controllers.dto.CreateTaskDto
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.commands.project.assignTask
import ru.quipy.logic.commands.project.changeTaskStatus
import ru.quipy.logic.commands.project.createTask
import ru.quipy.logic.commands.project.unAssignTask
import ru.quipy.logic.states.ProjectAggregateState
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/projects/{projectId}/tasks")
class TaskController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping
    fun createTask(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @RequestBody @Valid dto: CreateTaskDto
    ): TaskCreatedEvent {
        return projectEsService.update(projectId) { it.createTask(userId, dto.name) }
    }

    @PostMapping("/{taskId}/assignees")
    fun assignTask(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @RequestParam assigneeId: UUID
    ): TaskAssignedEvent {
        return projectEsService.update(projectId) { it.assignTask(userId, taskId, assigneeId) }
    }

    @DeleteMapping("/{taskId}/assignees/{assigneeId}")
    fun unAssignTask(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @PathVariable assigneeId: UUID
    ): TaskUnassignedEvent {
        return projectEsService.update(projectId) { it.unAssignTask(userId, taskId, assigneeId) }
    }

    @PutMapping("/{taskId}/status")
    fun changeTaskStatus(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @RequestParam taskStatusId: UUID
    ): TaskStatusChangedEvent {
        return projectEsService.update(projectId) { it.changeTaskStatus(userId, taskId, taskStatusId) }
    }
}