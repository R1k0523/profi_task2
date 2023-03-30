package com.example.task2.model

data class GroupShortResponse(
    val id: Long? = null,
    val name: String,
    val description: String?,
) {
    constructor(group: Group) : this(
        group.id,
        group.name,
        group.description,
    )
}
