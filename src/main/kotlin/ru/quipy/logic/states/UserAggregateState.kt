package ru.quipy.logic.states

import ru.quipy.api.UserAggregate
import ru.quipy.api.events.user.NicknameChangedEvent
import ru.quipy.api.events.user.UserCreatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate> {
    lateinit var userId: UUID
    lateinit var nickname: String
    lateinit var realName: String
    lateinit var passwordHash: String
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    override fun getId() = userId

    @StateTransitionFunc
    fun apply(event: UserCreatedEvent) {
        userId = event.userId
        nickname = event.nickname
        realName = event.realName
        passwordHash = event.passwordHash
        createdAt = event.createdAt
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun apply(event: NicknameChangedEvent) {
        nickname = event.nickname
        updatedAt = event.createdAt
    }
}