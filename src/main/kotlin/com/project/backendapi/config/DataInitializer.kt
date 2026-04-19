package com.project.backendapi.config

import com.project.backendapi.domain.entity.Post
import com.project.backendapi.domain.entity.User
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
        // admin 계정이 없으면 생성
        if (!userRepository.existsByUsername("admin")) {
            val adminUser = User(
                email = "admin@community.local",
                username = "admin",
                password = passwordEncoder.encode("1234") as String
            )
            val savedAdmin = userRepository.save(adminUser)

            // 샘플 게시글 추가
            val now = LocalDateTime.now()
            postRepository.save(
                Post(
                    title = "첫 번째 게시글입니다",
                    content = "환영합니다! 이것은 테스트 게시글입니다.",
                    author = savedAdmin,
                    createdAt = now,
                    updatedAt = now
                )
            )

            postRepository.save(
                Post(
                    title = "두 번째 게시글",
                    content = "커뮤니티에 오신 것을 환영합니다.",
                    author = savedAdmin,
                    createdAt = now.minusDays(1),
                    updatedAt = now.minusDays(1),
                    viewCount = 5
                )
            )

            postRepository.save(
                Post(
                    title = "공지사항",
                    content = "게시판 규칙을 지켜주세요.",
                    author = savedAdmin,
                    createdAt = now.minusDays(2),
                    updatedAt = now.minusDays(2),
                    viewCount = 10
                )
            )
        }
    }
}
