package com.project.backendapi.controller

import com.project.backendapi.domain.dto.DigitalProductRequestRequest
import com.project.backendapi.domain.dto.DigitalProductRequestResponse
import com.project.backendapi.service.DigitalProductRequestService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/digital-products")
class DigitalProductRequestController(
    private val requestService: DigitalProductRequestService
) {
    @PostMapping("/request")
    fun requestProduct(@Valid @RequestBody request: DigitalProductRequestRequest): ResponseEntity<DigitalProductRequestResponse> {
        val response = requestService.createRequest(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
