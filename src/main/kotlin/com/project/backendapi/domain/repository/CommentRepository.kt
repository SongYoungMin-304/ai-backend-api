package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostIdOrderByCreatedAtDesc(postId: Long): List<Comment>
    fun findByAuthorId(authorId: Long): List<Comment>
}
