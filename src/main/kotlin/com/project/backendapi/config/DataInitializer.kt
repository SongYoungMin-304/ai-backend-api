package com.project.backendapi.config

import com.project.backendapi.domain.entity.Post
import com.project.backendapi.domain.entity.User
import com.project.backendapi.domain.entity.UserRole
import com.project.backendapi.domain.repository.PostRepository
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        if (!userRepository.existsByUsername("admin")) {
            val adminUser = User(
                email = "admin@community.local",
                username = "admin",
                password = passwordEncoder.encode("1234") as String,
                role = UserRole.ADMIN
            )
            userRepository.save(adminUser)
        }
    }
}
