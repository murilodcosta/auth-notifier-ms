package dev.murilodcosta.auth_service.repository;

import dev.murilodcosta.auth_service.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
