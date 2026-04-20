package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CreatePostRequest
import com.project.backendapi.domain.dto.PostResponse
import com.project.backendapi.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun getPosts(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
        authentication: Authentication?
    ): ResponseEntity<Page<PostResponse>> {
        val userId = (authentication?.principal as? Long)
        println("🔐 PostController.getPosts: authentication=$authentication, principal=${authentication?.principal}, userId=$userId")
        return ResponseEntity.ok(postService.getPosts(pageable, userId))
    }

    @GetMapping("/{id}")
    fun getPostDetail(
        @PathVariable id: Long,
        authentication: Authentication?
    ): ResponseEntity<PostResponse> {
        val userId = (authentication?.principal as? Long)
        println("🔐 PostController.getPostDetail: authentication=$authentication, principal=${authentication?.principal}, userId=$userId")
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
