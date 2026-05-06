package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.DigitalProductRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DigitalProductRequestRepository : JpaRepository<DigitalProductRequest, Long>
