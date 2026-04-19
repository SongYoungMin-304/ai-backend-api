package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts", indexes = [
    Index(name = "idx_author_id", columnList = "author_id"),
    Index(name = "idx_created_at", columnList = "created_at")
])
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now(),

    var viewCount: Int = 0,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()
)
