package com.example.task2.repository

import com.example.task2.model.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository: JpaRepository<Participant, Long> {
    fun findAllByRecipient_Id(id: Long): List<Participant>
}
