package com.example.employee_management.config;

import com.example.employee_management.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public các API xác thực, Web View, Actuator
                        .requestMatchers("/api/auth/**", "/employees/**", "/actuator/**").permitAll()

                        // USER và ADMIN đều có thể xem danh sách nhân viên
                        .requestMatchers(HttpMethod.GET, "/api/v2/employees/**").hasAnyRole("USER", "ADMIN")

                        // Chỉ ADMIN mới được phép Thêm, Sửa, Xóa nhân viên
                        .requestMatchers(HttpMethod.POST, "/api/v2/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v2/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v2/employees/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
