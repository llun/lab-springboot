package dev.llun.practice.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class BlogController {

    @GetMapping("/")
    fun hello(model: Model):String {
        return "hello"
    }
}