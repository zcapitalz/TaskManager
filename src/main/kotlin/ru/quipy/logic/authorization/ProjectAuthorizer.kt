package ru.quipy.logic.authorization

import ru.quipy.logic.states.ProjectAggregateState
import java.util.*

class ProjectAuthorizer {
    companion object {
        fun checkUserIsProjectMember(project: ProjectAggregateState, userId: UUID) {
            if (project.creatorId != userId && !project.memberIds.contains(userId)) {
                throw IllegalArgumentException("User(id=$userId) is not a member of Project(id=${project.projectId})")
            }
        }
    }
}