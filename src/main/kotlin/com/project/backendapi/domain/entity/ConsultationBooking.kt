package com.project.backendapi.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "consultation_bookings")
class ConsultationBooking(
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
    @Column(nullable = false, name = "service_type")
    var serviceType: ServiceType = ServiceType.RESUME,

    @Column(columnDefinition = "TEXT", nullable = true)
    var message: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class ServiceType {
    RESUME,
    INTERVIEW,
    FULL
}
