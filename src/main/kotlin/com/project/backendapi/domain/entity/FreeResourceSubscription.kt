package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "free_resource_subscriptions")
class FreeResourceSubscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
