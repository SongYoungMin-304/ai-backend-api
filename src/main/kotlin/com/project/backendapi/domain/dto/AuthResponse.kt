package com.project.backendapi.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.project.backendapi.domain.entity.UserRole
import java.time.LocalDateTime

data class AuthResponse(
    val id: Long,
    val email: String,
    val username: String,
    @JsonProperty("isMasterAccount")
    val isMasterAccount: Boolean = false,
    val tier: String = "FREE",
    val accessToken: String,
    val refreshToken: String,
    val createdAt: LocalDateTime
)
