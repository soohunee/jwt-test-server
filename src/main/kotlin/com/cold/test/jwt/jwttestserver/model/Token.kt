package com.cold.test.jwt.jwttestserver.model

enum class Token(val expiration: Int) {
    ACCESS(1000 * 60 * 3), // 3min
    REFRESH(1000 * 60 * 5) // 5min
}