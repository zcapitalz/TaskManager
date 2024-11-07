package ru.quipy.logic.commands.project

import ru.quipy.api.events.project.TaskStatusCreatedEvent
import ru.quipy.api.events.project.TaskStatusDeletedEvent
import ru.quipy.logic.states.ProjectAggregateState
import ru.quipy.logic.authorization.ProjectAuthorizer.Companion.checkUserIsProjectMember
import java.util.*

fun ProjectAggregateState.createTaskStatus(currentUserId: UUID, name: String, colorRgb: Int): TaskStatusCreatedEvent {
    checkUserIsProjectMember(this, currentUserId)
    return TaskStatusCreatedEvent(UUID.randomUUID(), getId(), name, colorRgb)
}

fun ProjectAggregateState.deleteTaskStatus(currentUserId: UUID, taskStatusId: UUID): TaskStatusDeletedEvent {
    checkUserIsProjectMember(this, currentUserId)

    if (!availableTaskStatuses.containsKey(taskStatusId)) {
        throw IllegalArgumentException("Project(id=$projectId) does not contain available TaskStatus(id=$taskStatusId)")
    }

    if (tasks.any { t -> t.value.taskStatusId == taskStatusId }) {
        throw IllegalArgumentException("Cannot delete task status $taskStatusId because there are associated tasks")
    }

    return TaskStatusDeletedEvent(taskStatusId, getId())
}
