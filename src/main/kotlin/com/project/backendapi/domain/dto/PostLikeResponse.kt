package com.project.backendapi.domain.dto

data class PostLikeResponse(
    val postId: Long,
    val likeCount: Long,
    val liked: Boolean,
    val message: String = ""
)
