package com.project.backendapi.controller

import com.project.backendapi.domain.dto.PostLikeResponse
import com.project.backendapi.service.PostLikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostLikeController(
    private val postLikeService: PostLikeService
) {

    @PostMapping("/{postId}/likes")
    fun addLike(
        @PathVariable postId: Long,
        authentication: Authentication?
    ): ResponseEntity<PostLikeResponse> {
        if (authentication == null || authentication.principal == null) {
            throw IllegalArgumentException("인증이 필요합니다")
        }

        val userId = authentication.principal as? Long
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val response = postLikeService.addLike(postId, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/{postId}/likes")
    fun removeLike(
        @PathVariable postId: Long,
        authentication: Authentication?
    ): ResponseEntity<PostLikeResponse> {
        if (authentication == null || authentication.principal == null) {
            throw IllegalArgumentException("인증이 필요합니다")
        }

        val userId = authentication.principal as? Long
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")

        val response = postLikeService.removeLike(postId, userId)
        return ResponseEntity.ok(response)
    }
}
