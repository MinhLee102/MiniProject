package com.example.employee_management.controller;

import com.example.employee_management.entity.UserEntity;
import com.example.employee_management.repository.UserRepository;
import com.example.employee_management.service.CustomUserDetailsService;
import com.example.employee_management.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    // 1. Đăng ký tài khoản mới (Mặc định role: ROLE_USER nếu không truyền ROLE_ADMIN)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username đã tồn tại!");
        }

        String role = (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN"))
                ? "ROLE_ADMIN" : "ROLE_USER";

        UserEntity user = new UserEntity(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký tài khoản thành công với quyền: " + role);
    }

    // 2. Đăng nhập trả về JWT Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        response.put("username", userDetails.getUsername());
        response.put("authorities", userDetails.getAuthorities());

        return ResponseEntity.ok(response);
    }
}