package ru.quipy.api.events.user

import ru.quipy.api.UserAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID

const val USER_CREATED_EVENT_NAME = "USER_CREATED_EVENT"
const val NICKNAME_CHANGED_EVENT_NAME = "USER_NAME_CHANGED_EVENT"

@DomainEvent(USER_CREATED_EVENT_NAME)
class UserCreatedEvent(
    val userId: UUID,
    val nickname: String,
    val realName: String,
    val passwordHash: String,
    createdAt: Long = System.currentTimeMillis()
) : Event<UserAggregate>(
    name = USER_CREATED_EVENT_NAME,
    createdAt = createdAt
)

@DomainEvent(NICKNAME_CHANGED_EVENT_NAME)
class NicknameChangedEvent(
    val userId: UUID,
    val nickname: String,
    createdAt: Long = System.currentTimeMillis()
) : Event<UserAggregate>(
    name = NICKNAME_CHANGED_EVENT_NAME,
    createdAt = createdAt
)