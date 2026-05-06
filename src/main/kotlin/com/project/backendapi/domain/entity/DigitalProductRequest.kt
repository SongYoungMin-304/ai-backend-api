package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class DigitalProductType {
    RESUME_TEMPLATE,
    INTERVIEW_QUESTIONS,
    SALARY_NEGOTIATION
}

@Entity
@Table(name = "digital_product_requests")
class DigitalProductRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var phone: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var productType: DigitalProductType = DigitalProductType.RESUME_TEMPLATE,

    @Column(columnDefinition = "TEXT", nullable = true)
    var message: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
