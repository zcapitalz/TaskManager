package ru.quipy.logic.commands.user

import at.favre.lib.crypto.bcrypt.BCrypt
import ru.quipy.api.events.user.NicknameChangedEvent
import ru.quipy.api.events.user.UserCreatedEvent
import ru.quipy.logic.states.UserAggregateState
import java.util.*

const val PASSWORD_HASHING_COST = 10;

fun UserAggregateState.create(nickname: String, realName: String, password: String): UserCreatedEvent {
    val hashPassword = BCrypt.withDefaults().hashToString(PASSWORD_HASHING_COST, password.toCharArray())
    return UserCreatedEvent(UUID.randomUUID(), nickname, realName, hashPassword)
}

fun UserAggregateState.changeName(nickname: String): NicknameChangedEvent {
//    if (this.nickname == nickname) {
//        throw IllegalArgumentException("User(id=$id) already has nickname $nickname")
//    }

    // TODO: check nickname is not occupied by someone else

    return NicknameChangedEvent(userId, nickname)
}