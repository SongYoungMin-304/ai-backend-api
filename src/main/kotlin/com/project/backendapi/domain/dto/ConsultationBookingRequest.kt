package com.project.backendapi.domain.dto

data class ConsultationBookingRequest(
    val name: String,
    val email: String,
    val phone: String,
    val serviceType: String,
    val message: String? = null
)
