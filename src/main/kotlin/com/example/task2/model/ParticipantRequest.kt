package com.example.task2.model
data class ParticipantRequest(
    val name: String,
    val wish: String?,
) {
    fun toParticipant() = Participant(
        name = name,
        wish = wish,
    )
}
