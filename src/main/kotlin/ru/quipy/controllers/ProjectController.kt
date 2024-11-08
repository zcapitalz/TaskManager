package ru.quipy.controllers

import org.springframework.web.bind.annotation.*
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.UserAggregate
import ru.quipy.api.events.project.ProjectCreatedEvent
import ru.quipy.api.events.project.ProjectNameChangedEvent
import ru.quipy.api.events.project.ProjectMemberAddedEvent
import ru.quipy.api.events.project.ProjectMemberRemovedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.commands.project.addMember
import ru.quipy.logic.commands.project.changeName
import ru.quipy.logic.commands.project.create
import ru.quipy.logic.commands.project.removeMember
import ru.quipy.logic.states.ProjectAggregateState
import ru.quipy.logic.states.UserAggregateState
import java.util.*
import javax.validation.constraints.Size

@RestController
@RequestMapping("/projects")
class ProjectController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping
    fun createProject(
        @RequestParam
        userId: UUID,
        @RequestParam
        @Size(min = 2, max = 50, message = "Title must be from 2 to 50 characters long")
        projectName: String
    ) : ProjectCreatedEvent {
        if (userEsService.getState(userId) == null) {
            throw IllegalArgumentException("User(id=$userId) was not found")
        }

        return projectEsService.create { it.create(userId, projectName) }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PutMapping("/{projectId}/name")
    fun changeName(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @RequestParam
        @Size(min = 2, max = 50, message = "Title must be from 2 to 50 characters long")
        newName: String
    ) : ProjectNameChangedEvent {
        return projectEsService.update(projectId) { it.changeName(userId, newName) }
    }

    @PostMapping("/{projectId}/members")
    fun addProjectMember(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @RequestParam newMemberId: UUID
    ): ProjectMemberAddedEvent {
        return projectEsService.update(projectId) { it.addMember(userId, newMemberId) }
    }

    @DeleteMapping("/{projectId}/members/{memberToRemoveId}")
    fun removeProjectMember(
        @RequestParam userId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable memberToRemoveId: UUID,
    ): ProjectMemberRemovedEvent {
        return projectEsService.update(projectId) { it.removeMember(userId, memberToRemoveId) }
    }
}