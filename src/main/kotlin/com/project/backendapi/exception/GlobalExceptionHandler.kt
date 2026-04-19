package com.project.backendapi.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = "BAD_REQUEST",
            message = ex.message ?: "잘못된 요청입니다",
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .map { "${it.field}: ${it.defaultMessage}" }
            .joinToString(", ")

        val errorResponse = ErrorResponse(
            code = "VALIDATION_ERROR",
            message = message,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        ex.printStackTrace()
        val errorResponse = ErrorResponse(
            code = "INTERNAL_SERVER_ERROR",
            message = ex.message ?: "서버 오류가 발생했습니다",
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class ErrorResponse(
    val code: String,
    val message: String,
    val timestamp: LocalDateTime,
    val path: String
)
