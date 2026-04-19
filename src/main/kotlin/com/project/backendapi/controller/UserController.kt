package com.project.backendapi.controller

import com.project.backendapi.domain.dto.UserResponse
import com.project.backendapi.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{id}")
    fun getUserProfile(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PutMapping("/{id}")
    fun updateUserProfile(
        @PathVariable id: Long,
        @RequestBody updateRequest: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            userService.updateUser(id, updateRequest.bio, updateRequest.username, updateRequest.profileImage)
        )
    }
}

data class UpdateUserRequest(
    val bio: String?,
    val username: String?,
    val profileImage: String?
)
