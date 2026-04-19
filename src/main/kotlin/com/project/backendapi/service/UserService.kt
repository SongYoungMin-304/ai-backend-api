package com.project.backendapi.service

import com.project.backendapi.domain.dto.UserResponse
import com.project.backendapi.domain.entity.User
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow {
            IllegalArgumentException("사용자를 찾을 수 없습니다")
        }

        return UserResponse(
            id = user.id!!,
            email = user.email,
            username = user.username,
            profileImage = user.profileImage,
            bio = user.bio,
            createdAt = user.createdAt,
            postCount = user.posts.size,
            commentCount = user.comments.size
        )
    }

    @Transactional
    fun updateUser(id: Long, bio: String?, username: String?, profileImage: String?): UserResponse {
        val user = userRepository.findById(id).orElseThrow {
            IllegalArgumentException("사용자를 찾을 수 없습니다")
        }

        if (username != null && username != user.username) {
            if (userRepository.existsByUsername(username)) {
                throw IllegalArgumentException("이미 사용 중인 사용자명입니다")
            }
            // Kotlin의 data class는 이뮤터블이므로, entity 클래스를 변경하거나 다른 방식을 사용해야 함
            // 여기서는 User 클래스를 일반 클래스로 변경했으므로 직접 수정 가능
        }

        bio?.let { user.bio = it }
        profileImage?.let { user.profileImage = it }

        val updatedUser = userRepository.save(user)

        return UserResponse(
            id = updatedUser.id!!,
            email = updatedUser.email,
            username = updatedUser.username,
            profileImage = updatedUser.profileImage,
            bio = updatedUser.bio,
            createdAt = updatedUser.createdAt,
            postCount = updatedUser.posts.size,
            commentCount = updatedUser.comments.size
        )
    }
}
