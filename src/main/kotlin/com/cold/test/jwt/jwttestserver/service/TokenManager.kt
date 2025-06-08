package com.cold.test.jwt.jwttestserver.service

import com.cold.test.jwt.jwttestserver.exception.NotRefreshTokenException
import com.cold.test.jwt.jwttestserver.model.Token
import com.cold.test.jwt.jwttestserver.model.TokenRequestType
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*


@Service
class TokenManager {
    private val signingKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))
    private var CACHED_REFRESH_TOKEN: String? = null

    fun generateTokens(): Pair<String, String> {
        val accessToken = generateToken(Token.ACCESS)
        val refreshToken = generateToken(Token.REFRESH)

        CACHED_REFRESH_TOKEN = refreshToken

        return accessToken to refreshToken
    }

    fun generateToken(token: Token): String {
        val now = Date()
        val expiration = Date(now.time + token.expiration)

        return Jwts.builder()
            .subject(USER_ID.toString())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(signingKey)
            .compact()
    }

    fun validateTokenAndExtract(token: String, tokenRequestType: TokenRequestType): Long {
        if (tokenRequestType == TokenRequestType.REFRESH && token != CACHED_REFRESH_TOKEN) {
            throw NotRefreshTokenException()
        }

        val claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject.toLong()
    }

    companion object {
        const val USER_ID = 503L
        const val SECRET_KEY = "SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY"

    }
}