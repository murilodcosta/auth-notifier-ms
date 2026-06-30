package dev.murilodcosta.auth_service.service;

import dev.murilodcosta.auth_service.config.RabbitMQConfig;
import dev.murilodcosta.auth_service.config.TokenProvider;
import dev.murilodcosta.auth_service.domain.entities.User;
import dev.murilodcosta.auth_service.domain.enums.Role;
import dev.murilodcosta.auth_service.dto.LoginRequestDto;
import dev.murilodcosta.auth_service.dto.RegisterRequestDto;
import dev.murilodcosta.auth_service.dto.TokenResponseDto;
import dev.murilodcosta.auth_service.dto.events.UserRegisteredEventDto;
import dev.murilodcosta.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;

    private final RabbitTemplate rabbitTemplate;

    public void register(RegisterRequestDto registerRequestDto, String role) {
        // Check if user already exists
        if (userRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken");
        }

        User savedUser = userRepository.save(User.builder()
                .name(registerRequestDto.username())
                .email(registerRequestDto.email())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerRequestDto.password()))
                .build()
        );

        // Sending the asynchronous event to RabbitMQ
        var event = new UserRegisteredEventDto(
                savedUser.getName(),
                savedUser.getEmail()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) throws Exception{
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password())
            );

            String token = tokenProvider.generateToken(authentication);
            return TokenResponseDto.builder()
                    .token(token)
                    .expiresIn(expirationTime)
                    .build();
        }catch (BadCredentialsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
        }catch (Exception e){
            throw new Exception("An error occurred while trying to authenticate the user: " + e.getMessage());
        }
    }
}
