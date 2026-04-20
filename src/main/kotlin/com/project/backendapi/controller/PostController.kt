package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CreatePostRequest
import com.project.backendapi.domain.dto.PostResponse
import com.project.backendapi.security.JwtProvider
import com.project.backendapi.service.PostService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
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

    @GetMapping
    fun getPosts(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<Page<PostResponse>> {
        val userId = extractUserIdFromHeader(request)
        println("🔐 PostController.getPosts: userId=$userId")
        return ResponseEntity.ok(postService.getPosts(pageable, userId))
    }

    @GetMapping("/{id}")
    fun getPostDetail(
        @PathVariable id: Long,
        request: HttpServletRequest
    ): ResponseEntity<PostResponse> {
        val userId = extractUserIdFromHeader(request)
        println("🔐 PostController.getPostDetail: userId=$userId")
        return ResponseEntity.ok(postService.getPostById(id, userId))
    }

    @PostMapping
    fun createPost(
        @Valid @RequestBody request: CreatePostRequest,
        authentication: Authentication
    ): ResponseEntity<PostResponse> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.createPost(userId, request))
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<Map<String, String>> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")
        postService.deletePost(id, userId)
        return ResponseEntity.ok(mapOf("message" to "게시글이 삭제되었습니다"))
    }
}
