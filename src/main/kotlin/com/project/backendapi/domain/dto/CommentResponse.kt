package com.project.backendapi.domain.dto

import com.project.backendapi.domain.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val postId: Long,
    val content: String,
    val author: UserSimpleResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val parentCommentId: Long? = null,
    val replies: List<CommentResponse> = emptyList()
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                postId = comment.post!!.id!!,
                content = comment.content,
                author = UserSimpleResponse.from(comment.author!!),
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                parentCommentId = comment.parentComment?.id,
                replies = comment.replies.map { from(it) }
            )
        }
    }
}
