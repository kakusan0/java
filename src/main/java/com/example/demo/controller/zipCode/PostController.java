package com.example.demo.controller.zipCode;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/list")
    public String home(HttpSession session
            , Authentication authentication, Model model
    ) {
        session.setAttribute("username", "testUser");
        return "postList";
    }

}