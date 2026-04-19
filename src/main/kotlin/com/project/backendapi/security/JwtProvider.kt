package com.project.backendapi.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,

    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long
) {
    private val key by lazy { Keys.hmacShaKeyFor(jwtSecret.toByteArray()) }

    fun generateAccessToken(userId: Long, email: String, username: String): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .claim("username", username)
            .claim("type", "ACCESS")
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(userId: Long): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", "REFRESH")
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + refreshExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): Long? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            claims.subject.toLongOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun getTokenType(token: String): String? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            claims.get("type", String::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
