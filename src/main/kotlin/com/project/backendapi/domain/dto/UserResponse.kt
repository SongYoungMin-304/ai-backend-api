package com.project.backendapi.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.project.backendapi.domain.entity.UserTier
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val profileImage: String?,
    val bio: String?,
    val tier: UserTier,
    @JsonProperty("isMasterAccount")
    val isMasterAccount: Boolean,
    val createdAt: LocalDateTime,
    val postCount: Int,
    val commentCount: Int
)
