package dev.murilodcosta.auth_service.controller;

import dev.murilodcosta.auth_service.domain.entities.User;
import dev.murilodcosta.auth_service.dto.UserResponseDto;
import dev.murilodcosta.auth_service.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponseDto> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/me")
    public UserResponseDto getMyUser(@AuthenticationPrincipal User user) {
        return UserResponseDto.fromEntity(user);
    }

}
