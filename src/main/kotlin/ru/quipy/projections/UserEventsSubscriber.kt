package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.events.user.UserCreatedEvent
import ru.quipy.api.events.user.NicknameChangedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class UserEventsSubscriber {

    private val logger: Logger = LoggerFactory.getLogger(UserEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "user-events-subscriber") {

            `when`(UserCreatedEvent::class) { e ->
                logger.info("User(Id={}) created", e.userId)
            }

            `when`(NicknameChangedEvent::class) { e ->
                logger.info("User(Id={}) changed nickname to {}", e.userId, e.nickname)
            }
        }
    }
}