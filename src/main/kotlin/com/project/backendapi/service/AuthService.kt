package com.project.backendapi.service

import com.project.backendapi.domain.dto.AuthResponse
import com.project.backendapi.domain.dto.LoginRequest
import com.project.backendapi.domain.dto.SignupRequest
import com.project.backendapi.domain.entity.User
import com.project.backendapi.domain.repository.UserRepository
import com.project.backendapi.security.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {

    fun signup(request: SignupRequest): AuthResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("이미 사용 중인 사용자명입니다")
        }

        val user = User(
            email = "${request.username}@community.local",
            username = request.username,
            password = passwordEncoder.encode(request.password) as String
        )

        val savedUser = userRepository.save(user)
        val userId = savedUser.id ?: throw IllegalStateException("사용자 생성 실패")

        return AuthResponse(
            id = userId,
            email = savedUser.email,
            username = savedUser.username,
            accessToken = jwtProvider.generateAccessToken(userId, savedUser.email, savedUser.username),
            refreshToken = jwtProvider.generateRefreshToken(userId),
            createdAt = savedUser.createdAt
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("사용자명 또는 비밀번호가 올바르지 않습니다")

        // 계정 잠금 확인
        if (user.lockedUntil != null && user.lockedUntil!! > LocalDateTime.now()) {
            throw IllegalArgumentException("계정이 잠금되었습니다. 잠시 후 다시 시도해주세요")
        }

        // 계정 잠금 해제 (시간이 지난 경우)
        if (user.lockedUntil != null && user.lockedUntil!! <= LocalDateTime.now()) {
            user.lockedUntil = null
            user.loginAttempts = 0
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            user.loginAttempts++
            if (user.loginAttempts >= 5) {
                user.lockedUntil = LocalDateTime.now().plusMinutes(10)
            }
            userRepository.save(user)
            throw IllegalArgumentException("사용자명 또는 비밀번호가 올바르지 않습니다")
        }

        // 로그인 성공
        user.loginAttempts = 0
        user.lockedUntil = null
        user.lastLoginAt = LocalDateTime.now()
        val updatedUser = userRepository.save(user)

        return AuthResponse(
            id = updatedUser.id!!,
            email = updatedUser.email,
            username = updatedUser.username,
            accessToken = jwtProvider.generateAccessToken(updatedUser.id!!, updatedUser.email, updatedUser.username),
            refreshToken = jwtProvider.generateRefreshToken(updatedUser.id!!),
            createdAt = updatedUser.createdAt
        )
    }

    fun refreshToken(refreshToken: String): AuthResponse {
        if (!jwtProvider.validateToken(refreshToken) || jwtProvider.getTokenType(refreshToken) != "REFRESH") {
            throw IllegalArgumentException("유효하지 않은 Refresh Token입니다")
        }

        val userId = jwtProvider.getUserIdFromToken(refreshToken)
            ?: throw IllegalArgumentException("유효하지 않은 Refresh Token입니다")

        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("사용자를 찾을 수 없습니다")
        }

        return AuthResponse(
            id = user.id!!,
            email = user.email,
            username = user.username,
            accessToken = jwtProvider.generateAccessToken(user.id!!, user.email, user.username),
            refreshToken = jwtProvider.generateRefreshToken(user.id!!),
            createdAt = user.createdAt
        )
    }
}
