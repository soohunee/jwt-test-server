package com.cold.test.jwt.jwttestserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtTestServerApplication

fun main(args: Array<String>) {
    runApplication<JwtTestServerApplication>(*args)
}
