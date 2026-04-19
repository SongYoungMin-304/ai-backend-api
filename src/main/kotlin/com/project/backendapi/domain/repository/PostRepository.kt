package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByAuthorIdOrderByCreatedAtDesc(authorId: Long, pageable: Pageable): Page<Post>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>
}
