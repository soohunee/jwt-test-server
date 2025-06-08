package com.cold.test.jwt.jwttestserver.model

enum class TokenRequestType {
    NO_TOKEN,
    NORMAL,
    REFRESH;

    companion object {
        fun of(requestUri: String): TokenRequestType {
            if (requestUri.startsWith("/noToken")) {
                return NO_TOKEN
            }

            return if (requestUri.startsWith("/needToken/refresh")) {
                REFRESH
            } else {
                NORMAL
            }
        }
    }
}