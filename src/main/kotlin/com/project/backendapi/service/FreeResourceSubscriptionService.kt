package com.project.backendapi.service

import com.project.backendapi.domain.dto.FreeResourceSubscriptionRequest
import com.project.backendapi.domain.entity.FreeResourceSubscription
import com.project.backendapi.domain.repository.FreeResourceSubscriptionRepository
import org.springframework.stereotype.Service

@Service
class FreeResourceSubscriptionService(
    private val subscriptionRepository: FreeResourceSubscriptionRepository
) {
    fun subscribe(request: FreeResourceSubscriptionRequest) {
        if (request.email.isBlank()) {
            throw IllegalArgumentException("이메일은 필수입니다")
        }

        val subscription = FreeResourceSubscription(email = request.email)
        subscriptionRepository.save(subscription)
    }
}
