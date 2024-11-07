package ru.quipy.logic.states

import ru.quipy.api.ProjectAggregate
import ru.quipy.api.events.project.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

val DEFAULT_TASK_STATUS = TaskStatusEntity(
    id = UUID.fromString("991eac22-a734-4a08-8f61-038c08feaf33"),
    name = "Created",
    colorRgb = 0x000000)

class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    lateinit var projectId: UUID
    lateinit var name: String
    lateinit var creatorId: UUID
    var memberIds = mutableSetOf<UUID>()
    var availableTaskStatuses = mutableMapOf<UUID, TaskStatusEntity>()
    var tasks = mutableMapOf<UUID, TaskEntity>()
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    override fun getId() = projectId

    @StateTransitionFunc
    fun apply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        name = event.projectName
        creatorId = event.creatorId
        availableTaskStatuses[DEFAULT_TASK_STATUS.id] = DEFAULT_TASK_STATUS
        createdAt = event.createdAt
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: ProjectNameChangedEvent) {
        name = event.projectName
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: ProjectMemberAddedEvent) {
        memberIds.add(event.userId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: ProjectMemberRemovedEvent) {
        memberIds.remove(event.userId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskStatusCreatedEvent) {
        availableTaskStatuses[event.taskStatusId] = TaskStatusEntity(event.taskStatusId, event.taskName, event.colorRgb)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskStatusDeletedEvent) {
        availableTaskStatuses.remove(event.taskStatusId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskCreatedEvent) {
        tasks[event.taskId] = TaskEntity(event.taskId, event.taskName, event.taskStatusId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskAssignedEvent) {
        tasks.getValue(event.taskId).assigneeIds.add(event.userId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskUnassignedEvent) {
        tasks.getValue(event.taskId).assigneeIds.remove(event.userId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: TaskStatusChangedEvent) {
        tasks.getValue(event.taskId).taskStatusId = event.taskStatusId
        updatedAt = event.createdAt
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var taskStatusId: UUID,
    val assigneeIds: MutableSet<UUID> = mutableSetOf()
)

data class TaskStatusEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val colorRgb: Int
)