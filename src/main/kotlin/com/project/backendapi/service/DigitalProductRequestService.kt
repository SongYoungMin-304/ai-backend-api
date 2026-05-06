package com.project.backendapi.service

import com.project.backendapi.domain.dto.DigitalProductRequestRequest
import com.project.backendapi.domain.dto.DigitalProductRequestResponse
import com.project.backendapi.domain.entity.DigitalProductRequest
import com.project.backendapi.domain.entity.DigitalProductType
import com.project.backendapi.domain.repository.DigitalProductRequestRepository
import org.springframework.stereotype.Service

@Service
class DigitalProductRequestService(
    private val requestRepository: DigitalProductRequestRepository
) {
    fun createRequest(request: DigitalProductRequestRequest): DigitalProductRequestResponse {
        if (request.name.isBlank() || request.email.isBlank() || request.phone.isBlank() || request.productType.isBlank()) {
            throw IllegalArgumentException("필수 항목이 누락되었습니다")
        }

        val productType = when (request.productType.uppercase()) {
            "RESUME" -> DigitalProductType.RESUME_TEMPLATE
            "INTERVIEW" -> DigitalProductType.INTERVIEW_QUESTIONS
            "SALARY" -> DigitalProductType.SALARY_NEGOTIATION
            else -> throw IllegalArgumentException("잘못된 상품 타입입니다")
        }

        val entity = DigitalProductRequest(
            name = request.name,
            email = request.email,
            phone = request.phone,
            productType = productType,
            message = request.message?.takeIf { it.isNotBlank() }
        )

        val saved = requestRepository.save(entity)
        return toResponse(saved)
    }

    private fun toResponse(entity: DigitalProductRequest): DigitalProductRequestResponse {
        return DigitalProductRequestResponse(
            id = entity.id!!,
            name = entity.name,
            email = entity.email,
            phone = entity.phone,
            productType = entity.productType.name.lowercase(),
            message = entity.message,
            createdAt = entity.createdAt
        )
    }
}
