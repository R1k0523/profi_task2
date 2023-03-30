package com.example.task2.model

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table
data class Participant(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val name: String,
    val wish: String?,
    @OneToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    val recipient: Participant? = null,
)
