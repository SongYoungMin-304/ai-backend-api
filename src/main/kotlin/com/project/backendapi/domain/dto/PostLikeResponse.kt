package com.project.backendapi.domain.dto

data class PostLikeResponse(
    val postId: Long,
    val likeCount: Long,
    val isLiked: Boolean,
    val message: String = ""
)
