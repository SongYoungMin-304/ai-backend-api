package com.project.backendapi.domain.dto

import com.project.backendapi.domain.entity.User

data class UserSimpleResponse(
    val id: Long,
    val username: String,
    val profileImage: String?
) {
    companion object {
        fun from(user: User): UserSimpleResponse {
            return UserSimpleResponse(
                id = user.id!!,
                username = user.username,
                profileImage = user.profileImage
            )
        }
    }
}
