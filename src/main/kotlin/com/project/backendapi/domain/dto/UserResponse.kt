package com.project.backendapi.domain.dto

import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val profileImage: String?,
    val bio: String?,
    val createdAt: LocalDateTime,
    val postCount: Int,
    val commentCount: Int
)
