package com.project.backendapi.controller

import com.project.backendapi.domain.dto.PostLikeResponse
import com.project.backendapi.security.JwtProvider
import com.project.backendapi.service.PostLikeService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostLikeController(
    private val postLikeService: PostLikeService,
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

    @PostMapping("/{postId}/likes")
    fun addLike(
        @PathVariable postId: Long,
        request: HttpServletRequest
    ): ResponseEntity<PostLikeResponse> {
        val userId = extractUserIdFromHeader(request)
            ?: throw IllegalArgumentException("인증이 필요합니다")

        println("✅ PostLikeController.addLike: userId=$userId, postId=$postId")
        val response = postLikeService.addLike(postId, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/{postId}/likes")
    fun removeLike(
        @PathVariable postId: Long,
        request: HttpServletRequest
    ): ResponseEntity<PostLikeResponse> {
        val userId = extractUserIdFromHeader(request)
            ?: throw IllegalArgumentException("인증이 필요합니다")

        println("✅ PostLikeController.removeLike: userId=$userId, postId=$postId")
        val response = postLikeService.removeLike(postId, userId)
        return ResponseEntity.ok(response)
    }
}
