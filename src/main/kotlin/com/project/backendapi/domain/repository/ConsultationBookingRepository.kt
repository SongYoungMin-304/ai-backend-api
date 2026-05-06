package com.project.backendapi.domain.repository

import com.project.backendapi.domain.entity.ConsultationBooking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConsultationBookingRepository : JpaRepository<ConsultationBooking, Long>
