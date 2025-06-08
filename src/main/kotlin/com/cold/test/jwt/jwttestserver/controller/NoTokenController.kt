package com.cold.test.jwt.jwttestserver.controller

import com.cold.test.jwt.jwttestserver.service.TokenManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/noToken")
class NoTokenController(
    private val tokenManager: TokenManager
) {
    @GetMapping("/tokens")
    fun getTokens(): GetTokensResponse {
        val (accessToken, refreshToken) = tokenManager.generateTokens()

        return GetTokensResponse(accessToken, refreshToken)
    }
}

