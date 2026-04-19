package com.project.backendapi.domain.dto

import java.time.LocalDateTime

data class AuthResponse(
    val id: Long,
    val email: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val createdAt: LocalDateTime
)
