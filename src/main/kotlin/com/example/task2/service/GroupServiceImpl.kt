package com.example.task2.service

import com.example.task2.model.*
import com.example.task2.repository.GroupRepository
import com.example.task2.repository.ParticipantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class GroupServiceImpl(
    val groupRepo: GroupRepository,
    val participantRepo: ParticipantRepository,
): GroupService {
    override fun getGroups(): List<Group> = groupRepo.findAll()

    override fun saveGroup(group: Group): Group? = try {
        groupRepo.save(group)
    } catch (e: Exception) {
        null
    }

    override fun getGroupWithParticipants(id: Long): GroupFullResponse? = groupRepo.findByIdOrNull(id)?.let {
        GroupFullResponse(it, participantRepo.findAllById(it.participantsIds))
    }
    private fun getGroup(id: Long): Group? = groupRepo.findByIdOrNull(id)

    override fun updateGroup(id: Long, group: Group): Group? = getGroup(id)?.let {
        groupRepo.save(
            it.copy(
                name = group.name,
                description = group.description,
            )
        )
    }

    override fun deleteGroup(id: Long) {
        groupRepo.deleteById(id)
    }

    override fun deleteParticipant(groupId: Long, participantId: Long) {
        val group = getGroup(groupId) ?: throw NoSuchElementException()
        participantRepo.deleteById(participantId)
        participantRepo.findAllByRecipient_Id(participantId).forEach {
            saveParticipant(it.id!!, it.copy(recipient = null))
        }
        saveGroup(group.copy(participantsIds = group.participantsIds.filterNot { it == participantId }))
    }

    override fun saveParticipant(id: Long, participant: Participant): Participant? {
        val group = getGroup(id) ?: return null
        val saved = participantRepo.save(participant)
        saveGroup(
            group.copy(participantsIds = group.participantsIds + saved.id!!)
        )
        return saved
    }

    override fun toss(id: Long): GroupFullResponse? {
        val group = getGroup(id) ?: return null
        if (!group.canBeTossed())
            throw ConflictException()
        val shuffled = group.participantsIds.toParticipants().shuffled()
        val resulted = mutableListOf<Participant>()
        shuffled.forEachIndexed { index, participant ->
            resulted.add(
                participant.copy(
                    recipient = shuffled[(index+1)%shuffled.size]
                )
            )
        }
        participantRepo.saveAll(resulted)
        val savedGroup = saveGroup(group.copy(participantsIds = resulted.map { it.id!! })) ?: return null
        return GroupFullResponse(savedGroup, savedGroup.participantsIds.toParticipants())

    }

    fun List<Long>.toParticipants() = participantRepo.findAllById(this)

    override fun getRecipient(groupId: Long, participantId: Long): Participant? {
        val group = getGroup(groupId) ?: return null
        return participantRepo.findByIdOrNull(
            group.participantsIds.firstOrNull { it == participantId } ?: return null
        )?.recipient
    }

}
