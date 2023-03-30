package com.example.task2.service

import com.example.task2.model.*

interface GroupService {
    fun getGroups(): List<Group>
    fun saveGroup(group: Group): Group?
    fun getGroupWithParticipants(id: Long): GroupFullResponse?
    fun updateGroup(id: Long, group: Group): Group?
    fun deleteGroup(id: Long)
    fun deleteParticipant(groupId: Long, participantId: Long)
    fun saveParticipant(id: Long, participant: Participant): Participant?
    fun toss(id: Long): GroupFullResponse?
    fun getRecipient(groupId: Long, participantId: Long): Participant?

}
