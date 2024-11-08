package ru.quipy.api.events.project

import ru.quipy.api.ProjectAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val PROJECT_CREATED_EVENT_NAME = "PROJECT_CREATED_EVENT"
const val PROJECT_NAME_CHANGED_EVENT_NAME = "PROJECT_NAME_CHANGED_EVENT"
const val PROJECT_MEMBER_ADDED_EVENT_NAME = "PROJECT_MEMBER_ADDED_EVENT"
const val PROJECT_MEMBER_REMOVED_EVENT_NAME = "PROJECT_MEMBER_REMOVED_EVENT"

@DomainEvent(PROJECT_CREATED_EVENT_NAME)
class ProjectCreatedEvent(
    val projectId: UUID,
    val projectName: String,
    val creatorId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT_NAME,
    createdAt = createdAt
)

@DomainEvent(PROJECT_NAME_CHANGED_EVENT_NAME)
class ProjectNameChangedEvent(
    val projectId: UUID,
    val projectName: String,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = PROJECT_NAME_CHANGED_EVENT_NAME,
    createdAt = createdAt
)

@DomainEvent(PROJECT_MEMBER_ADDED_EVENT_NAME)
class ProjectMemberAddedEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = PROJECT_MEMBER_ADDED_EVENT_NAME,
    createdAt = createdAt
)

@DomainEvent(PROJECT_MEMBER_REMOVED_EVENT_NAME)
class ProjectMemberRemovedEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = PROJECT_MEMBER_REMOVED_EVENT_NAME,
    createdAt = createdAt
)