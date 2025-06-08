package com.cold.test.jwt.jwttestserver.filter

import com.cold.test.jwt.jwttestserver.exception.ErrorCode
import com.cold.test.jwt.jwttestserver.model.TokenRequestType
import com.cold.test.jwt.jwttestserver.service.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean


@Component
class TokenFilter(
    private val tokenManager: TokenManager
) : GenericFilterBean() {
    private val MAPPER = ObjectMapper().registerKotlinModule()

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val httpRequest = servletRequest as HttpServletRequest
        val requestUri = httpRequest.requestURI
        val tokenRequestType = TokenRequestType.of(requestUri)

        if (tokenRequestType == TokenRequestType.NO_TOKEN) {
            filterChain.doFilter(servletRequest, servletResponse)
        } else {
            val token = httpRequest.getHeader(AUTHORIZATION)
            if (token == null) {
                setErrorResponse(servletResponse as HttpServletResponse, ErrorCode.NO_TOKEN)
                return
            }
            runCatching {
                val userId = tokenManager.validateTokenAndExtract(token, tokenRequestType)
                servletRequest.setAttribute("userId", userId)
            }.onFailure { exception ->
                val errorCode = when (exception) {
                    is ExpiredJwtException -> ErrorCode.EXPIRED_TOKEN
                    else -> ErrorCode.INVALID_TOKEN
                }
                setErrorResponse(servletResponse as HttpServletResponse, errorCode)
                return
            }.onSuccess {
                filterChain.doFilter(servletRequest, servletResponse)
            }

        }
    }

    private fun setErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.status = errorCode.httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8" // 한글 깨짐 방지

        val errorResponse = ErrorResponse(errorCode.code, errorCode.message)

        val jsonResponse: String = MAPPER.writeValueAsString(errorResponse)
        response.writer.write(jsonResponse)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}

data class ErrorResponse(
    val errorCode: String,
    val message: String
)