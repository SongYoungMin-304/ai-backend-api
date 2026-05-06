package com.project.backendapi.controller

import com.project.backendapi.domain.dto.FreeResourceSubscriptionRequest
import com.project.backendapi.service.FreeResourceSubscriptionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/free-resources")
class FreeResourceSubscriptionController(
    private val subscriptionService: FreeResourceSubscriptionService
) {
    @PostMapping("/subscribe")
    fun subscribe(@Valid @RequestBody request: FreeResourceSubscriptionRequest): ResponseEntity<Map<String, String>> {
        subscriptionService.subscribe(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("message" to "구독해주셨습니다"))
    }
}
