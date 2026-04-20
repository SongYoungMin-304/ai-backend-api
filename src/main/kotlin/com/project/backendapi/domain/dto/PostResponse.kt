package com.project.backendapi.domain.dto

import com.project.backendapi.service.UserSimpleResponse
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: UserSimpleResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Long = 0,
    val isLiked: Boolean = false
)
