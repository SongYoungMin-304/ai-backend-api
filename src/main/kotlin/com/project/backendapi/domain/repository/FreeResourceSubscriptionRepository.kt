package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.FreeResourceSubscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FreeResourceSubscriptionRepository : JpaRepository<FreeResourceSubscription, Long>
