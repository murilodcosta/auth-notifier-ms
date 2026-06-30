package dev.murilodcosta.auth_service.service;

import dev.murilodcosta.auth_service.dto.UserResponseDto;
import dev.murilodcosta.auth_service.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponseDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDto::fromEntity);
    }
}
