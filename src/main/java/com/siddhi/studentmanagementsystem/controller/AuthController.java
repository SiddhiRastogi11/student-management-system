package com.siddhi.studentmanagementsystem.controller;

import com.siddhi.studentmanagementsystem.dto.AuthRequestDTO;
import com.siddhi.studentmanagementsystem.dto.AuthResponseDTO;
import com.siddhi.studentmanagementsystem.dto.RegisterRequestDTO;
import com.siddhi.studentmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService intelligenceAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return new ResponseEntity<>(intelligenceAuthService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(intelligenceAuthService.login(request));
    }
}