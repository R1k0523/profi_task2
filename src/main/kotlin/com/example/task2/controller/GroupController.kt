package com.example.task2.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.task2.model.*
import com.example.task2.service.GroupService

@RestController
class GroupController(val groupService: GroupService) {

    @PostMapping("/group")
    @Operation(
        summary = "Создать группу", responses = [
            ApiResponse(
                description = "ID группы",
                responseCode = "200",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = Long::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Объект не найден",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun saveGroup(@RequestBody group: GroupRequest) =
        groupService.saveGroup(group.toGroup())?.let {
            ResponseEntity.ok(it.id)
        } ?: ResponseEntity(
            "Не удалось сохранить объект",
            HttpStatus.INTERNAL_SERVER_ERROR
        )

    @GetMapping("/groups")
    @Operation(
        summary = "Получить список групп", responses = [
            ApiResponse(
                description = "Список групп",
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun getGroups() =
        ResponseEntity.ok(groupService.getGroups().map { GroupShortResponse(it) })

    @GetMapping("/group/{id}")
    @Operation(
        summary = "Получить группу по ID", responses = [
            ApiResponse(
                description = "Заданная группа",
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GroupFullResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Объект не найден",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun getGroup(@PathVariable id: Long) =
        groupService.getGroupWithParticipants(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity("Не найден", HttpStatus.NOT_FOUND)


    @PutMapping("/group/{id}")
    @Operation(
        summary = "Обновить группу по ID", responses = [
            ApiResponse(
                description = "Объект обновлен",
                responseCode = "204",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(hidden = true)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Объект не найден",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun updateGroup(
        @PathVariable id: Long,
        @RequestBody group: GroupRequest
    ) =
        groupService.updateGroup(id, group.toGroup())?.let {
            ResponseEntity.noContent().build()
        } ?: ResponseEntity("Не найден", HttpStatus.NOT_FOUND)


    @DeleteMapping("/group/{id}")
    @Operation(
        summary = "Удалить группу по ID", responses = [
            ApiResponse(
                description = "Объект удален",
                responseCode = "204",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(hidden = true)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Объект не найден",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun deleteGroup(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        groupService.deleteGroup(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/group/{id}/participant")
    @Operation(
        summary = "Добавить участника в группу по ID", responses = [
            ApiResponse(
                description = "ID участника",
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Long::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Группа не найдена",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun saveParticipant(
        @PathVariable id: Long,
        @RequestBody participant: ParticipantRequest
    ) =
        groupService.saveParticipant(id, participant.toParticipant())?.let {
            ResponseEntity.ok(it.id)
        } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @DeleteMapping("/group/{groupId}/participant/{participantId}")
    @Operation(
        summary = "Удалить участника из группы по ID", responses = [
            ApiResponse(
                description = "Объект удален",
                responseCode = "204",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(hidden = true)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Группа/участник не найдены",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun deleteParticipant(
        @PathVariable groupId: Long,
        @PathVariable participantId: Long
    ): ResponseEntity<String> {
        return try {
            groupService.deleteParticipant(groupId, participantId)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity("Участник с такими парметрами не найден", HttpStatus.NOT_FOUND)
        }
    }


    @PostMapping("/group/{id}/toss")
    @Operation(
        summary = "Провести жеребьевку", responses = [
            ApiResponse(
                description = "Результат жеребьевки",
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GroupFullResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Проведение жеребьевки невозможно",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun toss(@PathVariable id: Long) = try {
        groupService.toss(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)
    } catch (e: ConflictException) {
        ResponseEntity("Проведение жеребьевки невозможно", HttpStatus.CONFLICT)
    }

    @GetMapping("/group/{groupId}/participant/{participantId}/recipient")
    @Operation(
        summary = "Получить получателя участника из группы по ID", responses = [
            ApiResponse(
                description = "Участник, кому нужно дарить подарок",
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ParticipantShortResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Группа/участник не найдены",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка",
                content = [Content(
                    mediaType = "string",
                    schema = Schema(implementation = String::class)
                )]
            )]
    )
    fun getRecepient(
        @PathVariable groupId: Long,
        @PathVariable participantId: Long
    ) =
        groupService.getRecipient(groupId, participantId)?.let {
            ResponseEntity.ok(ParticipantShortResponse(it))
        } ?: ResponseEntity(
            "Участник с такими парметрами не найден",
            HttpStatus.NOT_FOUND
        )

}
