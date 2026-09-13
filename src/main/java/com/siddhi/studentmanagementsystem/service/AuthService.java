package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.dto.AuthRequestDTO;
import com.siddhi.studentmanagementsystem.dto.AuthResponseDTO;
import com.siddhi.studentmanagementsystem.dto.RegisterRequestDTO;
import com.siddhi.studentmanagementsystem.entity.Role;
import com.siddhi.studentmanagementsystem.entity.User;
import com.siddhi.studentmanagementsystem.exception.DuplicateResourceException;
import com.siddhi.studentmanagementsystem.repository.UserRepository;
import com.siddhi.studentmanagementsystem.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        // This now works perfectly without errors
        String token = jwtUtils.generateToken(user.getUsername());

        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // This also works perfectly now
        String token = jwtUtils.generateToken(authentication.getName());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}