package com.example.task2.model

data class GroupRequest(
    val name: String,
    val description: String?,
) {
    fun toGroup() = Group(
        name = name,
        description = description,
    )
}
