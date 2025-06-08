package com.cold.test.jwt.jwttestserver.controller

import com.cold.test.jwt.jwttestserver.service.TokenManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/needToken")
class NeedTokenController(
    private val tokenManager: TokenManager
) {
    @GetMapping("/test")
    fun needTokenTest(@RequestAttribute userId: Long): NeedTokenTestResponse {
        return NeedTokenTestResponse(userId)
    }

    @GetMapping("/refresh")
    fun refreshToken(@RequestAttribute userId: Long): GetTokensResponse {
        val (accessToken, refreshToken) = tokenManager.generateTokens()

        return GetTokensResponse(accessToken, refreshToken)
    }
}