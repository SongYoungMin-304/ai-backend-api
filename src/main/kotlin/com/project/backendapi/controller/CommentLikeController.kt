package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CommentLikeResponse
import com.project.backendapi.service.CommentLikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {

    @PostMapping("/{commentId}/likes")
    fun addLike(
        @PathVariable commentId: Long,
        authentication: Authentication?
    ): ResponseEntity<CommentLikeResponse> {
        if (authentication == null || authentication.principal == null) {
            throw IllegalArgumentException("인증이 필요합니다")
        }

        val userId = authentication.principal as? Long
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val response = commentLikeService.addLike(commentId, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/{commentId}/likes")
    fun removeLike(
        @PathVariable commentId: Long,
        authentication: Authentication?
    ): ResponseEntity<CommentLikeResponse> {
        if (authentication == null || authentication.principal == null) {
            throw IllegalArgumentException("인증이 필요합니다")
        }

        val userId = authentication.principal as? Long
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val response = commentLikeService.removeLike(commentId, userId)
        return ResponseEntity.ok(response)
    }
}
