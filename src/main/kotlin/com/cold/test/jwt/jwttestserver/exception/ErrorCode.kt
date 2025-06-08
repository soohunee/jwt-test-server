package com.cold.test.jwt.jwttestserver.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: String,
    val message: String
) {
    NO_TOKEN(HttpStatus.UNAUTHORIZED, "1", "No Authorization header"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "2", "Token expired"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "3", "Token is invalid")
}