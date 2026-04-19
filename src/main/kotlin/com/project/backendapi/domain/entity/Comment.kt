package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comments", indexes = [
    Index(name = "idx_post_id", columnList = "post_id"),
    Index(name = "idx_author_id", columnList = "author_id")
])
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now()
)
