package com.retentionos.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.retentionos.backend.dto.LoginRequest;
import com.retentionos.backend.dto.LoginResponse;
import com.retentionos.backend.service.BusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BusinessService businessService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return businessService.login(request.phone(), request.password());
    }
}
