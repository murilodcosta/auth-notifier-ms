package dev.murilodcosta.auth_service.controller;

import dev.murilodcosta.auth_service.dto.LoginRequestDto;
import dev.murilodcosta.auth_service.dto.RegisterRequestDto;
import dev.murilodcosta.auth_service.dto.TokenResponseDto;
import dev.murilodcosta.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    // Register user
    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequestDto userRegisterRequestDto, @RequestParam String role) throws Exception{
        authService.register(userRegisterRequestDto, role);
    }

    // Login user
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception{
        return authService.login(loginRequestDto);
    }
}
