package com.project.backendapi.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.project.backendapi.domain.entity.User
import com.project.backendapi.domain.entity.UserRole

data class UserSimpleResponse(
    val id: Long,
    val username: String,
    val profileImage: String?,
    val isAdmin: Boolean = false,
    @JsonProperty("isMasterAccount")
    val isMasterAccount: Boolean = false
) {
    companion object {
        fun from(user: User): UserSimpleResponse {
            val isAdmin = user.role == UserRole.ADMIN
            return UserSimpleResponse(
                id = user.id!!,
                username = user.username,
                profileImage = user.profileImage,
                isAdmin = isAdmin,
                isMasterAccount = isAdmin
            )
        }
    }
}
