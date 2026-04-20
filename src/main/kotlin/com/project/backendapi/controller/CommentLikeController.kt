package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CommentLikeResponse
import com.project.backendapi.security.JwtProvider
import com.project.backendapi.service.CommentLikeService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentLikeController(
    private val commentLikeService: CommentLikeService,
    private val jwtProvider: JwtProvider
) {

    private fun extractUserIdFromHeader(request: HttpServletRequest): Long? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            val token = bearerToken.substring(7)
            jwtProvider.getUserIdFromToken(token)
        } else {
            null
        }
    }

    @PostMapping("/{commentId}/likes")
    fun addLike(
        @PathVariable commentId: Long,
        request: HttpServletRequest
    ): ResponseEntity<CommentLikeResponse> {
        val userId = extractUserIdFromHeader(request)
            ?: throw IllegalArgumentException("인증이 필요합니다")

        println("✅ CommentLikeController.addLike: userId=$userId, commentId=$commentId")
        val response = commentLikeService.addLike(commentId, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/{commentId}/likes")
    fun removeLike(
        @PathVariable commentId: Long,
        request: HttpServletRequest
    ): ResponseEntity<CommentLikeResponse> {
        val userId = extractUserIdFromHeader(request)
            ?: throw IllegalArgumentException("인증이 필요합니다")

        println("✅ CommentLikeController.removeLike: userId=$userId, commentId=$commentId")
        val response = commentLikeService.removeLike(commentId, userId)
        return ResponseEntity.ok(response)
    }
}
