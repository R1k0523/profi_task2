package com.example.task2.model

import jakarta.persistence.*

@Entity
@Table(name="group_table")
data class Group(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val name: String,
    @Column(nullable = true)
    val description: String?,
    @ElementCollection
    val participantsIds: List<Long> = listOf()
) {
    fun canBeTossed() = participantsIds.size >= 3
}
