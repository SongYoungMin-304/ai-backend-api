package com.project.backendapi.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request)
            println("🔒 JwtAuthenticationFilter: token=${if (token != null) "exist" else "null"}, path=${request.requestURI}")

            if (token != null) {
                val isValid = jwtProvider.validateToken(token)
                println("🔒 Token valid: $isValid")

                if (isValid) {
                    val userId = jwtProvider.getUserIdFromToken(token)
                    println("🔒 UserId from token: $userId")

                    if (userId != null) {
                        val authentication = UsernamePasswordAuthenticationToken(
                            userId, null, emptyList()
                        )
                        SecurityContextHolder.getContext().authentication = authentication
                        println("🔒 Authentication set: userId=$userId")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("JWT 필터 처리 중 오류: ", e)
            println("❌ JWT Filter error: ${e.message}")
            e.printStackTrace()
        }
        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
