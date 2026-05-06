package com.project.backendapi.service

import com.project.backendapi.domain.dto.ConsultationBookingRequest
import com.project.backendapi.domain.dto.ConsultationBookingResponse
import com.project.backendapi.domain.entity.ConsultationBooking
import com.project.backendapi.domain.entity.ServiceType
import com.project.backendapi.domain.repository.ConsultationBookingRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class ConsultationBookingService(
    private val consultatonBookingRepository: ConsultationBookingRepository
) {

    fun createBooking(request: ConsultationBookingRequest): ConsultationBookingResponse {
        if (request.name.isBlank() || request.email.isBlank() || request.phone.isBlank() || request.serviceType.isBlank()) {
            throw IllegalArgumentException("필수 항목이 누락되었습니다")
        }

        val serviceType = when (request.serviceType.lowercase()) {
            "resume" -> ServiceType.RESUME
            "interview" -> ServiceType.INTERVIEW
            "full" -> ServiceType.FULL
            else -> throw IllegalArgumentException("잘못된 서비스 타입입니다")
        }

        val booking = ConsultationBooking(
            name = request.name,
            email = request.email,
            phone = request.phone,
            serviceType = serviceType,
            message = request.message?.takeIf { it.isNotBlank() }
        )

        val saved = consultatonBookingRepository.save(booking)
        return toResponse(saved)
    }

    fun getBookingById(id: Long): ConsultationBookingResponse {
        val booking = consultatonBookingRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다") }
        return toResponse(booking)
    }

    fun getAllBookings(): List<ConsultationBookingResponse> {
        return consultatonBookingRepository.findAll()
            .sortedByDescending { it.createdAt }
            .map { toResponse(it) }
    }

    private fun toResponse(entity: ConsultationBooking): ConsultationBookingResponse {
        return ConsultationBookingResponse(
            id = entity.id!!,
            name = entity.name,
            email = entity.email,
            phone = entity.phone,
            serviceType = entity.serviceType.name.lowercase(),
            message = entity.message,
            createdAt = entity.createdAt
        )
    }
}
