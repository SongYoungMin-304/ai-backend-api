package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post_likes", uniqueConstraints = [
    UniqueConstraint(columnNames = ["post_id", "user_id"], name = "uk_post_user")
], indexes = [
    Index(name = "idx_post_id", columnList = "post_id"),
    Index(name = "idx_user_id", columnList = "user_id")
])
class PostLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now()
)
