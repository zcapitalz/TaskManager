package ru.quipy.controllers.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateTaskStatusDto(
    @NotBlank
    @Size(min = 2, max = 50, message = "Name must be from 2 to 50 characters long")
    val name: String,

    @Min(value = 0, message = "Color RGB must be a non-negative value")
    val colorRgb: Int
)