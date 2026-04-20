package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CommentResponse
import com.project.backendapi.domain.dto.CreateCommentRequest
import com.project.backendapi.security.JwtProvider
import com.project.backendapi.service.CommentService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService,
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

    @GetMapping("/posts/{postId}/comments")
    fun getCommentsByPostId(
        @PathVariable postId: Long,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, List<CommentResponse>>> {
        val userId = extractUserIdFromHeader(request)
        println("🔐 CommentController.getCommentsByPostId: userId=$userId")
        val comments = commentService.getCommentsByPostId(postId, userId)
        return ResponseEntity.ok(mapOf("comments" to comments))
    }

    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @Valid @RequestBody request: CreateCommentRequest,
        authentication: Authentication
    ): ResponseEntity<CommentResponse> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val comment = commentService.createComment(postId, userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(comment)
    }

    @PostMapping("/comments/{commentId}/replies")
    fun createReply(
        @PathVariable commentId: Long,
        @Valid @RequestBody request: CreateCommentRequest,
        authentication: Authentication
    ): ResponseEntity<CommentResponse> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val reply = commentService.createReply(commentId, userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(reply)
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        authentication: Authentication
    ): ResponseEntity<Map<String, String>> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        commentService.deleteComment(commentId, userId)
        return ResponseEntity.ok(mapOf("message" to "댓글이 삭제되었습니다"))
    }
}
