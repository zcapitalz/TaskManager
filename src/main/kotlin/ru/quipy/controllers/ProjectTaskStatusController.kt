package ru.quipy.controllers

import org.springframework.web.bind.annotation.*
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.events.project.TaskStatusCreatedEvent
import ru.quipy.api.events.project.TaskStatusDeletedEvent
import ru.quipy.controllers.dto.CreateTaskStatusDto
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.commands.project.createTaskStatus
import ru.quipy.logic.commands.project.deleteTaskStatus
import ru.quipy.logic.states.ProjectAggregateState
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/projects/{projectId}/available-task-statuses")
class ProjectTaskStatusController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping
    fun createTaskStatus(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @RequestBody @Valid dto: CreateTaskStatusDto
    ): TaskStatusCreatedEvent {
        return projectEsService.update(projectId) { it.createTaskStatus(userId, dto.name, dto.colorRgb) }
    }

    @DeleteMapping("/{taskStatusId}")
    fun deleteTaskStatus(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable taskStatusId: UUID
    ): TaskStatusDeletedEvent {
        return projectEsService.update(projectId) { it.deleteTaskStatus(userId, taskStatusId) }
    }
}