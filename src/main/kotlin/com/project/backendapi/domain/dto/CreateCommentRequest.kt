package com.project.backendapi.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCommentRequest(
    @field:NotBlank(message = "내용은 필수입니다")
    @field:Size(max = 500, message = "500자 이하로 입력해주세요")
    val content: String
)
