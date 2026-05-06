package com.project.backendapi.controller

import com.project.backendapi.domain.dto.ConsultationBookingRequest
import com.project.backendapi.domain.dto.ConsultationBookingResponse
import com.project.backendapi.service.ConsultationBookingService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bookings")
class ConsultationBookingController(
    private val consultatonBookingService: ConsultationBookingService
) {

    @PostMapping
    fun createBooking(@Valid @RequestBody request: ConsultationBookingRequest): ResponseEntity<ConsultationBookingResponse> {
        val response = consultatonBookingService.createBooking(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable id: Long): ResponseEntity<ConsultationBookingResponse> {
        return ResponseEntity.ok(consultatonBookingService.getBookingById(id))
    }

    @GetMapping
    fun getAllBookings(): ResponseEntity<List<ConsultationBookingResponse>> {
        return ResponseEntity.ok(consultatonBookingService.getAllBookings())
    }
}
