package dev.llun.practice.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api")
class BlogController {

    @GetMapping("/hello")
    fun hello(model: Model):String {
        return "hello"
    }

    @GetMapping("/something/test")
    fun something(model: Model):String {
        return "something"
    }

}