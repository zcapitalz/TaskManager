package ru.quipy.logic.commands.project

import ru.quipy.api.events.project.TaskAssignedEvent
import ru.quipy.api.events.project.TaskCreatedEvent
import ru.quipy.api.events.project.TaskStatusChangedEvent
import ru.quipy.api.events.project.TaskUnassignedEvent
import ru.quipy.logic.states.ProjectAggregateState
import ru.quipy.logic.states.TaskEntity
import ru.quipy.logic.authorization.ProjectAuthorizer.Companion.checkUserIsProjectMember
import ru.quipy.logic.states.DEFAULT_TASK_STATUS
import java.util.*

fun ProjectAggregateState.createTask(userId: UUID, name: String): TaskCreatedEvent {
    checkUserIsProjectMember(this, userId)
    return TaskCreatedEvent(UUID.randomUUID(), getId(), name, DEFAULT_TASK_STATUS.id)
}

fun ProjectAggregateState.assignTask(userId: UUID, taskId: UUID, assigneeId: UUID): TaskAssignedEvent {
    checkUserIsProjectMember(this, userId)
    checkUserIsProjectMember(this, assigneeId)

    if (getTask(taskId).assigneeIds.contains(assigneeId)) {
        throw IllegalArgumentException("Task(id=$taskId) in Project(id=$projectId) already contains Assignee(id=$assigneeId)")
    }

    return TaskAssignedEvent(taskId, getId(), assigneeId)
}

fun ProjectAggregateState.unAssignTask(userId: UUID, taskId: UUID, assigneeId: UUID): TaskUnassignedEvent {
    checkUserIsProjectMember(this, userId)

    if (!getTask(taskId).assigneeIds.contains(assigneeId)) {
        throw IllegalArgumentException("Task(id=$taskId) in Project(id=$projectId) does not contain Assignee(id=$assigneeId)")
    }

    return TaskUnassignedEvent(taskId, getId(), assigneeId)
}

fun ProjectAggregateState.changeTaskStatus(userId: UUID, taskId: UUID, taskStatusId: UUID): TaskStatusChangedEvent {
    checkUserIsProjectMember(this, userId)

    if (!availableTaskStatuses.containsKey(taskStatusId)) {
        throw IllegalArgumentException("TaskStatus(id=$taskStatusId) is not available in Project(id=$projectId)")
    }

//    if (getTask(taskId).taskStatusId == taskStatusId) {
//        throw IllegalArgumentException("Task(id=$taskId) status is already TaskStatus(id=$taskStatusId)")
//    }

    return TaskStatusChangedEvent(taskId, getId(), taskStatusId)
}

private fun ProjectAggregateState.getTask(taskId: UUID): TaskEntity {
    return tasks[taskId] ?: throw IllegalArgumentException("Task(id=$taskId) does not exist in Project(id=$projectId)")
}