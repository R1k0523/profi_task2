package com.example.task2.model
data class GroupFullResponse(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val participants: List<ParticipantShortResponse>
) {

    constructor(group: Group, participants: List<Participant>) : this(
        group.id,
        group.name,
        group.description,
        participants.map { ParticipantShortResponse(it) }
    )
}
