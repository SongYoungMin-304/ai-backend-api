package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment_likes", uniqueConstraints = [
    UniqueConstraint(columnNames = ["comment_id", "user_id"], name = "uk_comment_user")
], indexes = [
    Index(name = "idx_comment_id", columnList = "comment_id"),
    Index(name = "idx_user_id", columnList = "user_id")
])
class CommentLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    val comment: Comment? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now()
)
