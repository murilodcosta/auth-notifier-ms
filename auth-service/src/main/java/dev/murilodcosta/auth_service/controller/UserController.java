package dev.murilodcosta.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @GetMapping
    public String getUsers() {
        return "List of users";
    }

    @GetMapping("/me")
    public String getMyUser() {
        return "My user";
    }

}
