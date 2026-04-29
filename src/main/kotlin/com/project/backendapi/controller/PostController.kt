package com.project.backendapi.controller

import com.project.backendapi.domain.dto.CreatePostRequest
import com.project.backendapi.domain.dto.PostResponse
import com.project.backendapi.security.JwtProvider
import com.project.backendapi.service.FileStorageService
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
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
    private val jwtProvider: JwtProvider,
    private val fileStorageService: FileStorageService
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
        @RequestParam("category", required = false) category: String?,
        request: HttpServletRequest
    ): ResponseEntity<Page<PostResponse>> {
        val userId = extractUserIdFromHeader(request)
        val categoryEnum = category?.let { 
            try {
                com.project.backendapi.domain.entity.Category.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        println("🔐 PostController.getPosts: userId=$userId, category=$categoryEnum")
        return ResponseEntity.ok(postService.getPosts(pageable, userId, categoryEnum))
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
        @RequestParam("title") title: String,
        @RequestParam("content") content: String,
        @RequestParam("category", required = false) category: String?,
        @RequestParam("image", required = false) image: MultipartFile?,
        authentication: Authentication
    ): ResponseEntity<PostResponse> {
        val userId = (authentication.principal as? Long)
            ?: throw IllegalArgumentException("사용자를 식별할 수 없습니다")
        
        val imageUrl = image?.let { fileStorageService.storeFile(it) }
        val categoryEnum = category?.let { 
            try {
                com.project.backendapi.domain.entity.Category.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        val request = CreatePostRequest(title, content, categoryEnum)
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.createPost(userId, request, imageUrl))
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

    @GetMapping("/{id}/neighbors")
    fun getNeighborPosts(
        @PathVariable id: Long
    ): ResponseEntity<Map<String, Any>> {
        val neighbors = postService.getNeighborPosts(id)
        return ResponseEntity.ok(mapOf(
            "success" to true,
            "data" to neighbors
        ))
    }
}
