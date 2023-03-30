package com.example.task2.model
data class ParticipantShortResponse(
    val id: Long? = null,
    val name: String,
    val wish: String?,
) {

    constructor(participant: Participant) : this(
        participant.id,
        participant.name,
        participant.wish,
    )
}
