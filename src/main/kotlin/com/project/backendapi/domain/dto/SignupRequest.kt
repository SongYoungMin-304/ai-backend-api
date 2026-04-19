package com.project.backendapi.domain.dto

import jakarta.validation.constraints.*

data class SignupRequest(
    @field:NotBlank(message = "사용자명은 필수입니다")
    @field:Size(min = 2, max = 20, message = "사용자명은 2자 이상 20자 이하여야 합니다")
    val username: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다")
    val password: String
)
