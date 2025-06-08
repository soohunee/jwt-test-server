package com.cold.test.jwt.jwttestserver.controller

data class GetTokensResponse(
    val accessToken: String,
    val refreshToken: String
)

data class NeedTokenTestResponse(
    val userId: Long
)