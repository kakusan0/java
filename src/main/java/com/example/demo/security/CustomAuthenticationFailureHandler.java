//package com.example.demo.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.MessageSource;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Locale;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    private final MessageSource messageSource;
//
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException {
//
//        String message = messageSource.getMessage("user.not.found", null, Locale.getDefault());
//        request.setAttribute("loginError", message);
//        request.getRequestDispatcher("/").forward(request, response);
//        log.error(message);
//
//    }
//}
