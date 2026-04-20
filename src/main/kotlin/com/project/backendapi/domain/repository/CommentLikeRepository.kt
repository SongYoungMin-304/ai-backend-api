package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.CommentLike
import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
    fun existsByCommentIdAndUserId(commentId: Long, userId: Long): Boolean
    fun findByCommentIdAndUserId(commentId: Long, userId: Long): CommentLike?
    fun deleteByCommentIdAndUserId(commentId: Long, userId: Long)
    fun countByCommentId(commentId: Long): Long
}
