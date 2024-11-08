package ru.quipy.logic.commands.project

import ru.quipy.api.events.project.ProjectCreatedEvent
import ru.quipy.api.events.project.ProjectNameChangedEvent
import ru.quipy.api.events.project.ProjectMemberAddedEvent
import ru.quipy.api.events.project.ProjectMemberRemovedEvent
import ru.quipy.logic.states.ProjectAggregateState
import ru.quipy.logic.authorization.ProjectAuthorizer.Companion.checkUserIsProjectMember
import java.util.*

fun ProjectAggregateState.create(userId: UUID, projectName: String): ProjectCreatedEvent {
    return ProjectCreatedEvent(UUID.randomUUID(), projectName, userId)
}

fun ProjectAggregateState.changeName(userId: UUID, name: String): ProjectNameChangedEvent {
    checkUserIsProjectMember(this, userId)
    return ProjectNameChangedEvent(getId(), name)
}

fun ProjectAggregateState.addMember(userId: UUID, memberToAddId: UUID): ProjectMemberAddedEvent {
    checkUserIsProjectMember(this, userId)

    if (creatorId == memberToAddId || memberIds.contains(memberToAddId)) {
        throw IllegalArgumentException("User(id=$memberToAddId) is already a member of Project(id=$projectId)")
    }

    return ProjectMemberAddedEvent(getId(), memberToAddId)
}

fun ProjectAggregateState.removeMember(userId: UUID, memberToRemoveId: UUID): ProjectMemberRemovedEvent {
    checkUserIsProjectMember(this, userId)

    if (creatorId == memberToRemoveId) {
        throw IllegalArgumentException("Cannot remove User(id=$memberToRemoveId) because he is the creator of Project(id=${projectId})")
    }

    if (!memberIds.contains(memberToRemoveId)) {
        throw IllegalArgumentException("User(id=$memberToRemoveId) is not a member of Project(id=$projectId)")
    }

    return ProjectMemberRemovedEvent(getId(), memberToRemoveId)
}

