package ru.quipy.api.events.project

import ru.quipy.api.ProjectAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TASK_STATUS_CREATED_EVENT_NAME = "TASK_STATUS_CREATED_EVENT"
const val TASK_STATUS_DELETED_EVENT_NAME = "TASK_STATUS_DELETED_EVENT"

@DomainEvent(TASK_STATUS_CREATED_EVENT_NAME)
class TaskStatusCreatedEvent(
    val taskStatusId: UUID,
    val projectId: UUID,
    val taskName: String,
    val colorRgb: Int,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = TASK_STATUS_CREATED_EVENT_NAME,
    createdAt = createdAt
)

@DomainEvent(TASK_STATUS_DELETED_EVENT_NAME)
class TaskStatusDeletedEvent(
    val taskStatusId: UUID,
    val projectId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = TASK_STATUS_DELETED_EVENT_NAME,
    createdAt = createdAt
)