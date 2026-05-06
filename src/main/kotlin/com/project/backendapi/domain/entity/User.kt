package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class UserRole {
    USER,
    ADMIN
}

enum class UserTier {
    FREE,
    PREMIUM,
    ENTERPRISE
}

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val email: String = "",

    @Column(unique = true, nullable = false)
    val username: String = "",

    @Column(nullable = false)
    var password: String = "",

    var profileImage: String? = null,
    var bio: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tier: UserTier = UserTier.FREE,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now(),

    var isActive: Boolean = true,
    var lastLoginAt: LocalDateTime? = null,
    var loginAttempts: Int = 0,
    var lockedUntil: LocalDateTime? = null,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "author", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()
)
