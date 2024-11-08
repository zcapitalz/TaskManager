package ru.quipy.controllers.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateTaskDto(
    @NotBlank
    @Size(min = 2, max = 50, message = "Title must be from 2 to 50 characters long")
    val name: String,
)