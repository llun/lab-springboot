package dev.llun.practice

import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPatternParser

fun main() {
    val patternMatcher = PathPatternParser().parse("/api/resource")
    println(patternMatcher.matches(PathContainer.parsePath("/api;something-else/resource")))
    println(patternMatcher.matches(PathContainer.parsePath("/api,something-else/resource")))
    println(patternMatcher.matches(PathContainer.parsePath("/api!something-else/resource")))
    println(patternMatcher.matches(PathContainer.parsePath("/api/resource")))
    println(patternMatcher.matches(PathContainer.parsePath("/api1/resource")))
}