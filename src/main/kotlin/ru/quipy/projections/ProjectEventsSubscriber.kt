package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.events.project.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    private val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "project-events-subscriber") {

            `when`(ProjectCreatedEvent::class) { e ->
                logger.info("Project created: {} (Id: {})", e.name, e.projectId)
            }

            `when`(ProjectNameChangedEvent::class) { e ->
                logger.info("Project name changed: {} (Id: {})", e.projectName, e.projectId)
            }

            `when`(ProjectMemberAddedEvent::class) { e ->
                logger.info("User added to project: User Id: {} (Project Id: {})", e.userId, e.projectId)
            }

            `when`(ProjectMemberRemovedEvent::class) { e ->
                logger.info("User removed from project: User Id: {} (Project Id: {})", e.userId, e.projectId)
            }


            `when`(TaskStatusCreatedEvent::class) { e ->
                logger.info("Task status created: {} (Id: {}) (Project Id: {})", e.taskName, e.taskStatusId, e.projectId)
            }

            `when`(TaskStatusDeletedEvent::class) { e ->
                logger.info("Task status deleted: Id: {} (Project Id: {})", e.taskStatusId, e.projectId)
            }


            `when`(TaskCreatedEvent::class) { e ->
                logger.info("Task created: {} (Id: {}) (Project Id: {})", e.taskName, e.taskId, e.projectId)
            }

            `when`(TaskAssignedEvent::class) { e ->
                logger.info("Task assigned: Task Id: {} to User ID: {} (Project Id: {})", e.taskId, e.userId, e.projectId)
            }

            `when`(TaskUnassignedEvent::class) { e ->
                logger.info("Task unassigned: Task ID: {} from User ID: {} (Project Id: {})", e.taskId, e.userId, e.projectId)
            }

            `when`(TaskStatusChangedEvent::class) { e ->
                logger.info("Task status changed: Task ID: {} (Task status ID: {}) (Project Id: {})", e.taskId, e.taskStatusId, e.projectId)
            }
        }
    }
}