package com.project.backendapi.domain.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class DigitalProductRequestRequest(
    val name: String,
    val email: String,
    val phone: String,
    val productType: String,
    val message: String? = null
)

data class DigitalProductRequestResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val productType: String,
    val message: String?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime
)
