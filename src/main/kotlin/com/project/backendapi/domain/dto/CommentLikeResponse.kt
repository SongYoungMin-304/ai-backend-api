package com.project.backendapi.domain.dto

data class CommentLikeResponse(
    val commentId: Long,
    val likeCount: Long,
    val liked: Boolean,
    val message: String = ""
)
