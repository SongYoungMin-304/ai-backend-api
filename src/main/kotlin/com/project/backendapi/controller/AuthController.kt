package com.project.backendapi.controller

import com.project.backendapi.domain.dto.AuthResponse
import com.project.backendapi.domain.dto.LoginRequest
import com.project.backendapi.domain.dto.SignupRequest
import com.project.backendapi.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.signup(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") authHeader: String): ResponseEntity<AuthResponse> {
        val token = authHeader.removePrefix("Bearer ")
        return ResponseEntity.ok(authService.refreshToken(token))
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "로그아웃되었습니다"))
    }
}
