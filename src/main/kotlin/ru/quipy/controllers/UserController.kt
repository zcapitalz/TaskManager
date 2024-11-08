package ru.quipy.controllers

import org.springframework.web.bind.annotation.*
import ru.quipy.api.UserAggregate
import ru.quipy.api.events.user.NicknameChangedEvent
import ru.quipy.api.events.user.UserCreatedEvent
import ru.quipy.controllers.dto.CreateUserDto
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.commands.user.changeName
import ru.quipy.logic.commands.user.create
import ru.quipy.logic.states.UserAggregateState
import java.util.*
import javax.validation.constraints.Size

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
) {
    @PostMapping
    fun createUser(@RequestBody dto: CreateUserDto): UserCreatedEvent {
        return userEsService.create { it.create(dto.nickname, dto.realName, dto.password) }
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): UserAggregateState? {
        return userEsService.getState(userId)
    }

    @PutMapping("/{userId}/nickname")
    fun changeNickname(
            @RequestParam
            userId: UUID,
            @RequestParam
            @Size(min = 2, max = 50, message = "User name must be from 2 to 50 symbols long")
            nickname: String
    ): NicknameChangedEvent {
        return userEsService.update(userId) { it.changeName(nickname) }
    }
}